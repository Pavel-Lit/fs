package com.geekbrains.model;

import java.io.Serializable;

public interface AbstractMessage extends Serializable {

    com.geekbrains.model.MessageType getMessageType();

}
