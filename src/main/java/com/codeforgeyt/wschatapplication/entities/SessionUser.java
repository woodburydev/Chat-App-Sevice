package com.codeforgeyt.wschatapplication.entities;

import java.util.UUID;

public class SessionUser {
    private String name;
    private String recipient;
    private String sessionId;

    public SessionUser(String name, String recipient, String sessionId) {
        this.name = name;
        this.recipient = recipient;
        this.sessionId = sessionId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String chattingWith) {
        this.recipient = chattingWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}
