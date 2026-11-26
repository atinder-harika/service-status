package com.atinder.service_status_backend.service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Service
public class HealthCheckService {

    private static final Duration CHECK_TIMEOUT = Duration.ofSeconds(5);

    private final ServiceRepository serviceRepository;
    private final WebClient webClient;
    private final MeterRegistry meterRegistry;
    private final Timer cycleTimer;
    private final AtomicInteger servicesUp = new AtomicInteger();
    private final AtomicInteger servicesDown = new AtomicInteger();
    // One virtual thread per check: a cycle takes as long as the slowest service, not the sum of all.
    private final ExecutorService checkExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public HealthCheckService(ServiceRepository serviceRepository) {
        this(serviceRepository, new SimpleMeterRegistry());
    }

    @Autowired
    public HealthCheckService(ServiceRepository serviceRepository, MeterRegistry meterRegistry) {
        this.serviceRepository = serviceRepository;
        this.meterRegistry = meterRegistry;
        // Timeouts at the socket level too, so a host that never answers can't hold a connection open.
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) CHECK_TIMEOUT.toMillis())
            .responseTimeout(CHECK_TIMEOUT);
        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
        this.cycleTimer = Timer.builder("service_status.check.cycle")
            .description("Time to check every monitored service once")
            .register(meterRegistry);
        meterRegistry.gauge("service_status.services", List.of(io.micrometer.core.instrument.Tag.of("status", "up")), servicesUp);
        meterRegistry.gauge("service_status.services", List.of(io.micrometer.core.instrument.Tag.of("status", "down")), servicesDown);
    }

    @Scheduled(fixedDelayString = "${monitor.check-interval-ms:30000}")
    public void checkAllServices() {
        try {
            List<MonitoredService> services = StreamSupport
                .stream(serviceRepository.findAll().spliterator(), false)
                .toList();
            cycleTimer.record(() -> runChecks(services));
            long up = services.stream().filter(s -> "Operational".equals(s.getCurrentStatus())).count();
            servicesUp.set((int) up);
            servicesDown.set(services.size() - (int) up);
        } catch (Exception e) {
            log.error("Health check cycle failed", e);
        }
    }

    private void runChecks(List<MonitoredService> services) {
        var futures = services.stream()
            .map(s -> checkExecutor.submit(() -> performHealthCheck(s)))
            .toList();
        for (var f : futures) {
            try {
                f.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                log.error("Health check task failed", e);
            }
        }
    }

    private void performHealthCheck(MonitoredService service) {
        long start = System.nanoTime();
        try {
            webClient.get()
                .uri(service.getUrl())
                .retrieve()
                .toBodilessEntity()
                // Backstop only: one second past the socket timeouts, so they fire first. With equal
                // timeouts the two race, and the losing connect error gets logged as onErrorDropped.
                .timeout(CHECK_TIMEOUT.plusSeconds(1))
                .block();
            service.setCurrentStatus("Operational");
        } catch (Exception e) {
            service.setCurrentStatus("Down");
        }
        meterRegistry.timer("service_status.check", "service", service.getName(), "status", service.getCurrentStatus())
            .record(Duration.ofNanos(System.nanoTime() - start));

        service.setLastCheckedAt(OffsetDateTime.now());
        serviceRepository.save(service);

        log.info("Health check completed for {}: {}", service.getName(), service.getCurrentStatus());
    }
}
