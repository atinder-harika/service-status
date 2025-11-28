package com.atinder.service_status_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "services")
public class MonitoredService {
    
    @Id
    private Long id;
    
    private String name;
    private String url;
    private String checkType;
    private String currentStatus;
    private OffsetDateTime lastCheckedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Custom constructor for creating new services (DB auto-generates id, createdAt, updatedAt)
    public MonitoredService(String name, String url, String checkType, String currentStatus) {
        this.name = name;
        this.url = url;
        this.checkType = checkType;
        this.currentStatus = currentStatus;
        this.lastCheckedAt = null; 
        this.createdAt = OffsetDateTime.now(); 
        this.updatedAt = OffsetDateTime.now();
    }
}