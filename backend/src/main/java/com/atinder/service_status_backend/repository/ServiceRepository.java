package com.atinder.service_status_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.atinder.service_status_backend.model.MonitoredService;

@Repository
public interface ServiceRepository extends CrudRepository<MonitoredService, Long> {
    // Spring Data JDBC auto-implements: save(), findById(), findAll(), deleteById()
    Optional<MonitoredService> findByName(String name);

    List<MonitoredService> findByCurrentStatus(String status);
    
}