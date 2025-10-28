package com.atinder.service_status_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceGroupDTO {
    private String title;
    private List<ServiceDTO> checks;
    private String status;
}