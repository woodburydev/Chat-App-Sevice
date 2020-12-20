package com.codeforgeyt.wschatapplication.dto;

public class AuthResponse {
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }

    private String jwt;
}
