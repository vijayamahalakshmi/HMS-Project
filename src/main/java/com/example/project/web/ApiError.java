package com.example.project.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private int status;                 // HTTP status code (e.g., 400)
    private String error;               // Reason phrase (e.g., "Bad Request")
    private String message;             // Human-readable message
    private String path;                // Request path
    private List<FieldValidationError> fieldErrors; // Optional per-field errors

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
