package com.atinder.service_status_backend.service;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthCheckService {

    private final ServiceRepository serviceRepository;
    private final WebClient webClient;

    public HealthCheckService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
        this.webClient  = WebClient.builder()
            .baseUrl("http://dummy")
            .build();
    }

    @Scheduled(fixedDelay = 30000)
    public void checkAllServices(){
        try{
            serviceRepository
            .findAll()
            .forEach(this::performHealthCheck);
        }catch(Exception e){
            System.err.printf("Encountered an error: %s%n",e.getMessage());
        }
    }

    private void performHealthCheck(MonitoredService service) {
        try {
            webClient.get()
                .uri(service.getUrl())
                .retrieve()
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(5)) // R4: 5s max
                .block(); // Block to wait for result (simple approach for MVP)
            
            // If we reach here, request succeeded
            service.setCurrentStatus("Operational");
        } catch (Exception e) {
            // Timeout or error
            service.setCurrentStatus("Down");
        }
        
        service.setLastCheckedAt(OffsetDateTime.now());
        serviceRepository.save(service);

        log.info("Health check completed for {}: {}", service.getName(), service.getCurrentStatus());
    }
}
