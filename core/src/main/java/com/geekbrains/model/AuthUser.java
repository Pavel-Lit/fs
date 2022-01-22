package com.geekbrains.model;

import lombok.Getter;

@Getter
public class AuthUser implements AbstractMessage {
    private final String name;
    private final String password;

    public AuthUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.AUTH_USER;
    }
}
