package com.codeforgeyt.wschatapplication.entities;

import java.sql.Timestamp;
import java.util.UUID;

public class SafeUser {
    private String username;
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Timestamp lastRequest;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Timestamp lastRequest) {
        this.lastRequest = lastRequest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SafeUser(String username, UUID id,  String firstName, String lastName, String email, Timestamp lastRequest) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastRequest = lastRequest;
    }
}
