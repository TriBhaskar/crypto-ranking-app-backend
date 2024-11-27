package org.triBhaskar.auth.model;

public class ForgotPasswordRequest {
    private String email;
    private String resetPwdUrl;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetPwdUrl() {
        return resetPwdUrl;
    }

    public void setResetPwdUrl(String resetPwdUrl) {
        this.resetPwdUrl = resetPwdUrl;
    }
}

