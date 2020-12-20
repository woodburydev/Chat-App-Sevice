package com.codeforgeyt.wschatapplication.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private UUID id;
    private Timestamp lastRequest;

    public Timestamp getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Timestamp lastRequest) {
        this.lastRequest = lastRequest;
    }

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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User(String username, String password, UUID id, String firstName, String lastName, String email, Timestamp lastRequest) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastRequest = lastRequest;
    }

}
