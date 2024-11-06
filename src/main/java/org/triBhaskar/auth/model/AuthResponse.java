package org.triBhaskar.auth.model;

public class AuthResponse {

    //AuthResponse(jwt, refreshToken)
    private String jwt;
    private String refreshToken;

    public AuthResponse() {
    }

    public AuthResponse(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

}
