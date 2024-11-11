package org.triBhaskar.auth.model;

import java.time.LocalDateTime;

public class RegisterResponse {
    private String status;
    private String message;
    private Long userId;
    private LocalDateTime timestamp;


    public RegisterResponse(String status, String message, Long userId, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                '}';
    }
}
