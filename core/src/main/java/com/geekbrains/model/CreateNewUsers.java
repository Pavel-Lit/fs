package com.geekbrains.model;


import lombok.Getter;

@Getter
public class CreateNewUsers implements AbstractMessage{
    private final String name;
    private final String password;

    public CreateNewUsers(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ADD_NEW_USER;
    }
}
