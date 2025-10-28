package com.atinder.service_status_backend.repository;

import com.atinder.service_status_backend.model.MonitoredService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServiceRepositoryTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void testSaveAndFindService() {
        // Given: Create a new service
        MonitoredService service = new MonitoredService(
            "Test Service",
            "https://example.com/health",
            "HTTP",
            "Unknown"
        );

        // When: Save to database
        MonitoredService saved = serviceRepository.save(service);

        // Then: Verify it was saved with auto-generated ID
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Service");

        // When: Find by name
        Optional<MonitoredService> found = serviceRepository.findByName("Test Service");

        // Then: Verify it exists
        assertThat(found).isPresent();
        assertThat(found.get().getUrl()).isEqualTo("https://example.com/health");

        // Cleanup
        serviceRepository.deleteById(saved.getId());
    }

    @Test
    void testFindByCurrentStatus() {
        // Given: Create services with different statuses
        MonitoredService service1 = new MonitoredService("Service 1", "https://example.com/1", "HTTP", "Operational");
        MonitoredService service2 = new MonitoredService("Service 2", "https://example.com/2", "HTTP", "Down");
        MonitoredService service3 = new MonitoredService("Service 3", "https://example.com/3", "HTTP", "Operational");

        serviceRepository.save(service1);
        serviceRepository.save(service2);
        serviceRepository.save(service3);

        // When: Find all operational services
        List<MonitoredService> operational = serviceRepository.findByCurrentStatus("Operational");

        // Then: Should find at least 2 services
        assertThat(operational).hasSizeGreaterThanOrEqualTo(2);

        // Cleanup
        serviceRepository.delete(service1);
        serviceRepository.delete(service2);
        serviceRepository.delete(service3);
    }
}