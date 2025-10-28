package com.atinder.service_status_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorResponse = Map.of(
            "timestamp", OffsetDateTime.now(),
            "status", ex.getStatusCode().value(),
            "error", ex.getStatusCode().toString(),
            "message", ex.getReason() != null ? ex.getReason() : "An error occurred"
        );
        
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = Map.of(
            "timestamp", OffsetDateTime.now(),
            "status", 500,
            "error", "Internal Server Error",
            "message", ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}