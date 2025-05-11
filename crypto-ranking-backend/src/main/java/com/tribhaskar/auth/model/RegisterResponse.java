package com.tribhaskar.auth.model;

import java.time.LocalDateTime;

public class RegisterResponse {
    private String status;
    private String message;
    private String username;
    private long otpValiditySeconds;
    private LocalDateTime timestamp;

    public RegisterResponse(String status, String message, String username, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.username = username;
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

    public long getOtpValiditySeconds() {
        return otpValiditySeconds;
    }

    public void setOtpValiditySeconds(long otpValiditySeconds) {
        this.otpValiditySeconds = otpValiditySeconds;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
