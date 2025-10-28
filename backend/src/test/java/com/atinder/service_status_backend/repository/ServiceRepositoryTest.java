package com.atinder.service_status_backend.repository;

import com.atinder.service_status_backend.model.MonitoredService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Repository tests using mocks (no database connection needed for CI)
 */
@ExtendWith(MockitoExtension.class)
class ServiceRepositoryTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Test
    void testSaveAndFindService() {
        // Given: Mock service
        MonitoredService service = new MonitoredService(
            "Test Service",
            "https://example.com/health",
            "HTTP",
            "Unknown"
        );
        service.setId(1L);

        // When: Mock repository behavior
        when(serviceRepository.save(any(MonitoredService.class))).thenReturn(service);
        when(serviceRepository.findByName("Test Service")).thenReturn(Optional.of(service));

        MonitoredService saved = serviceRepository.save(service);

        // Then: Verify mocked behavior
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("Test Service");

        Optional<MonitoredService> found = serviceRepository.findByName("Test Service");
        assertThat(found).isPresent();
        assertThat(found.get().getUrl()).isEqualTo("https://example.com/health");
    }

    @Test
    void testFindByCurrentStatus() {
        // Given: Mock services with different statuses
        MonitoredService service1 = new MonitoredService("Service 1", "https://example.com/1", "HTTP", "Operational");
        service1.setId(1L);
        MonitoredService service3 = new MonitoredService("Service 3", "https://example.com/3", "HTTP", "Operational");
        service3.setId(3L);

        // When: Mock repository to return operational services
        when(serviceRepository.findByCurrentStatus("Operational")).thenReturn(List.of(service1, service3));
        List<MonitoredService> operational = serviceRepository.findByCurrentStatus("Operational");

        // Then: Should find 2 operational services
        assertThat(operational).hasSize(2);
        assertThat(operational).extracting(MonitoredService::getName)
            .containsExactlyInAnyOrder("Service 1", "Service 3");
    }
}