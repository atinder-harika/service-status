package com.atinder.service_status_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple health check endpoint for deployment verification.
 * Accessible at /health or / (root)
 */
@RestController
public class HealthController {

    @GetMapping({"/", "/health", "/ping"})
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", Instant.now().toString());
        response.put("application", "service-status-backend");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}
