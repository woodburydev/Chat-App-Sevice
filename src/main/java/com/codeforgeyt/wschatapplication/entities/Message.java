package com.codeforgeyt.wschatapplication.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Message {
private String author;
private String message;
private String recipient;
private UUID ID;
private Boolean read;
private Timestamp timestamp;

    public Timestamp getDate() {
        return timestamp;
    }

    public void setDate(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Message(String author, String recipient, String message, Boolean read, UUID id, Timestamp timestamp) {
        this.author = author;
        this.message = message;
        this.ID = id;
        this.recipient = recipient;
        this.read = read;
        this.timestamp = timestamp;
    }
}
