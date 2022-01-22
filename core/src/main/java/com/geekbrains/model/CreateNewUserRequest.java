package com.geekbrains.model;

import lombok.Getter;

@Getter
public class CreateNewUserRequest implements AbstractMessage{

    private String newUserRequest;

    public CreateNewUserRequest(String newUserRequest) {
        this.newUserRequest = newUserRequest;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ADD_NEW_USER_REQUEST;
    }
}
