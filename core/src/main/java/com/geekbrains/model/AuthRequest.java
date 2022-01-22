package com.geekbrains.model;

import lombok.Data;

@Data
public class AuthRequest implements AbstractMessage {
    private final String authRequest;

    public AuthRequest(String authOK) {
        this.authRequest = authOK;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.AUTH_REQUEST;
    }
}
