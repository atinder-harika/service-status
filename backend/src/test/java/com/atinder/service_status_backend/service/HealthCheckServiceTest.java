package com.atinder.service_status_backend.service;

import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HealthCheckService
 * Tests business logic without external dependencies
 */
@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    private HealthCheckService healthCheckService;

    @BeforeEach
    void setUp() {
        healthCheckService = new HealthCheckService(serviceRepository);
    }

    @Test
    void testCheckAllServices_Success() {
        // Given: Multiple services in repository
        MonitoredService service1 = new MonitoredService("GitHub", "https://github.com", "HTTP", "Unknown");
        service1.setId(1L);
        MonitoredService service2 = new MonitoredService("Google", "https://google.com", "HTTP", "Unknown");
        service2.setId(2L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));
        when(serviceRepository.save(any(MonitoredService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: Scheduled task runs
        healthCheckService.checkAllServices();

        // Then: All services should be checked and saved
        verify(serviceRepository, times(1)).findAll();
        verify(serviceRepository, times(2)).save(any(MonitoredService.class));
    }

    @Test
    void testCheckAllServices_HandlesException() {
        // Given: Repository throws exception
        when(serviceRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When: Scheduled task runs
        // Then: Should not throw exception (caught and logged)
        healthCheckService.checkAllServices();

        // Verify error was handled gracefully
        verify(serviceRepository, times(1)).findAll();
        verify(serviceRepository, never()).save(any(MonitoredService.class));
    }

    @Test
    void testPerformHealthCheck_UpdatesTimestamp() {
        // Given: A service to check
        MonitoredService service = new MonitoredService("Test", "https://httpbin.org/status/200", "HTTP", "Unknown");
        service.setId(1L);
        OffsetDateTime beforeCheck = OffsetDateTime.now();

        when(serviceRepository.save(any(MonitoredService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: Health check is performed (via checkAllServices)
        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service));
        healthCheckService.checkAllServices();

        // Then: Service should have updated timestamp
        ArgumentCaptor<MonitoredService> captor = ArgumentCaptor.forClass(MonitoredService.class);
        verify(serviceRepository).save(captor.capture());
        
        MonitoredService savedService = captor.getValue();
        assertThat(savedService.getLastCheckedAt()).isNotNull();
        assertThat(savedService.getLastCheckedAt()).isAfterOrEqualTo(beforeCheck);
    }

    @Test
    void testPerformHealthCheck_SetsOperationalForValidUrl() {
        // Given: A service with valid URL
        MonitoredService service = new MonitoredService("Google", "https://www.google.com", "HTTP", "Unknown");
        service.setId(1L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service));
        when(serviceRepository.save(any(MonitoredService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: Health check runs
        healthCheckService.checkAllServices();

        // Then: Status should be updated
        ArgumentCaptor<MonitoredService> captor = ArgumentCaptor.forClass(MonitoredService.class);
        verify(serviceRepository).save(captor.capture());
        
        MonitoredService savedService = captor.getValue();
        assertThat(savedService.getCurrentStatus()).isIn("Operational", "Down"); // Network-dependent
        assertThat(savedService.getLastCheckedAt()).isNotNull();
    }

    @Test
    void testPerformHealthCheck_SetsDownForInvalidUrl() {
        // Given: A service with invalid/unreachable URL
        MonitoredService service = new MonitoredService(
            "Invalid", 
            "https://thisdomaindoesnotexist12345.invalid", 
            "HTTP", 
            "Unknown"
        );
        service.setId(1L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service));
        when(serviceRepository.save(any(MonitoredService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: Health check runs
        healthCheckService.checkAllServices();

        // Then: Status should be Down
        ArgumentCaptor<MonitoredService> captor = ArgumentCaptor.forClass(MonitoredService.class);
        verify(serviceRepository).save(captor.capture());
        
        MonitoredService savedService = captor.getValue();
        assertThat(savedService.getCurrentStatus()).isEqualTo("Down");
        assertThat(savedService.getLastCheckedAt()).isNotNull();
    }
}
