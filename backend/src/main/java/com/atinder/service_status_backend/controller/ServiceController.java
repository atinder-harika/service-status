package com.atinder.service_status_backend.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.atinder.service_status_backend.dto.ServiceDTO;
import com.atinder.service_status_backend.dto.ServiceGroupDTO;
import com.atinder.service_status_backend.model.MonitoredService;
import com.atinder.service_status_backend.repository.ServiceRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // TODO: Restrict in production to frontend domain
public class ServiceController {
    
    private final ServiceRepository serviceRepository;
    
    public ServiceController(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

   @GetMapping("/services")
    public List<ServiceGroupDTO> getAllServices() {
        // Fetch, convert to DTOs, and collect in one step
        List<ServiceDTO> serviceDTOs = StreamSupport.stream(serviceRepository.findAll().spliterator(), false)
            .map(this::convertToDTO)
            .collect(Collectors.toList());

        // Compute status and wrap in a single return statement
        return List.of(new ServiceGroupDTO(
            "All Services", 
            serviceDTOs, 
            computeGroupStatus(serviceDTOs)
        ));
    }
    
    
    @GetMapping("/services/{id}")
    public ServiceDTO getServiceById(@PathVariable Long id) {
        return serviceRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Service with id " + id + " not found"
            ));
    }

    private ServiceDTO convertToDTO(MonitoredService service) {
        return new ServiceDTO(
            service.getId(),
            service.getName(),
            service.getUrl(),
            service.getCheckType(),
            service.getCurrentStatus(),
            service.getLastCheckedAt()
        );
    }

    private String computeGroupStatus(List<ServiceDTO> checks) {
        boolean hasDown = checks.stream().anyMatch(c -> "Down".equals(c.getCurrentStatus()));
        boolean hasDegraded = checks.stream().anyMatch(c -> "Degraded".equals(c.getCurrentStatus()));
        
        if (hasDown) return "Down";
        if (hasDegraded) return "Degraded";
        return "Operational";
    }
}