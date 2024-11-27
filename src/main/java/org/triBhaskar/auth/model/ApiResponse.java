package org.triBhaskar.auth.model;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private String status;        // "success" or "error"
    private String message;       // Response message
    private T data;              // Generic type for data
    private LocalDateTime timestamp;  // Timestamp of response

    // Constructor for responses without data
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for responses with data
    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
