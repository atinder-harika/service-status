package com.atinder.service_status_backend.controller;

import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ServiceController
 * Tests HTTP endpoints with mocked repository
 */
@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceRepository serviceRepository;

    @Test
    void testGetAllServices_ReturnsServiceGroups() throws Exception {
        // Given: Services in repository
        MonitoredService service1 = new MonitoredService("GitHub", "https://github.com", "HTTP", "Operational");
        service1.setId(1L);
        service1.setLastCheckedAt(OffsetDateTime.now());

        MonitoredService service2 = new MonitoredService("Google", "https://google.com", "HTTP", "Down");
        service2.setId(2L);
        service2.setLastCheckedAt(OffsetDateTime.now());

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));

        // When & Then: GET /api/services
        mockMvc.perform(get("/api/services")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // One group
                .andExpect(jsonPath("$[0].title", is("All Services")))
                .andExpect(jsonPath("$[0].checks", hasSize(2)))
                .andExpect(jsonPath("$[0].checks[0].name", is("GitHub")))
                .andExpect(jsonPath("$[0].checks[0].currentStatus", is("Operational")))
                .andExpect(jsonPath("$[0].checks[1].name", is("Google")))
                .andExpect(jsonPath("$[0].checks[1].currentStatus", is("Down")))
                .andExpect(jsonPath("$[0].status", is("Down"))); // Group status is "Down" if any service is down
    }

    @Test
    void testGetAllServices_EmptyRepository() throws Exception {
        // Given: No services
        when(serviceRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then: GET /api/services
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].checks", hasSize(0)))
                .andExpect(jsonPath("$[0].status", is("Operational"))); // Empty group is "Operational"
    }

    @Test
    void testGetServiceById_Found() throws Exception {
        // Given: Service exists
        MonitoredService service = new MonitoredService("GitHub", "https://github.com", "HTTP", "Operational");
        service.setId(1L);
        service.setLastCheckedAt(OffsetDateTime.now());

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        // When & Then: GET /api/services/1
        mockMvc.perform(get("/api/services/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("GitHub")))
                .andExpect(jsonPath("$.url", is("https://github.com")))
                .andExpect(jsonPath("$.currentStatus", is("Operational")));
    }

    @Test
    void testGetServiceById_NotFound() throws Exception {
        // Given: Service does not exist
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then: GET /api/services/999 returns 404
        mockMvc.perform(get("/api/services/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void testGetAllServices_ComputesGroupStatus_Operational() throws Exception {
        // Given: All services operational
        MonitoredService service1 = new MonitoredService("Service1", "https://example1.com", "HTTP", "Operational");
        service1.setId(1L);
        MonitoredService service2 = new MonitoredService("Service2", "https://example2.com", "HTTP", "Operational");
        service2.setId(2L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));

        // When & Then: Group status should be "Operational"
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("Operational")));
    }

    @Test
    void testGetAllServices_ComputesGroupStatus_Degraded() throws Exception {
        // Given: Mix of operational and degraded services
        MonitoredService service1 = new MonitoredService("Service1", "https://example1.com", "HTTP", "Operational");
        service1.setId(1L);
        MonitoredService service2 = new MonitoredService("Service2", "https://example2.com", "HTTP", "Degraded");
        service2.setId(2L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));

        // When & Then: Group status should be "Degraded"
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("Degraded")));
    }

    @Test
    void testGetAllServices_ComputesGroupStatus_Down() throws Exception {
        // Given: At least one service is down
        MonitoredService service1 = new MonitoredService("Service1", "https://example1.com", "HTTP", "Operational");
        service1.setId(1L);
        MonitoredService service2 = new MonitoredService("Service2", "https://example2.com", "HTTP", "Down");
        service2.setId(2L);
        MonitoredService service3 = new MonitoredService("Service3", "https://example3.com", "HTTP", "Degraded");
        service3.setId(3L);

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2, service3));

        // When & Then: Group status should be "Down" (highest priority)
        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status", is("Down")));
    }
}
