package com.atinder.service_status_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long id;
    private String name;
    private String url;
    private String checkType;
    private String currentStatus;
    private OffsetDateTime lastCheckedAt;
}