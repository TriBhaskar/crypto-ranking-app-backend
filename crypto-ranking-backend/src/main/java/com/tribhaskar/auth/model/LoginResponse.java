package com.tribhaskar.auth.model;

import java.time.LocalDateTime;

public class LoginResponse {
    private String status;
    private String message;
    private String username;
    private String jwt;
    private LocalDateTime timestamp;

    public LoginResponse(String status, String message, String username, String jwt, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.username = username;
        this.jwt = jwt;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", jwt='" + jwt + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
