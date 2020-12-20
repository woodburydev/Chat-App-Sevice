package com.codeforgeyt.wschatapplication.entities;

import com.codeforgeyt.wschatapplication.config.ChatMessages;

public class ChatSessionResponse<T> {

    private String messageType;
    private T messageValue;

    public ChatSessionResponse(ChatMessages messageType, T messageValue) {
        this.messageType = messageType.name();
        this.messageValue = messageValue;
    }
}
