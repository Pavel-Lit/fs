package com.geekbrains.model;

import lombok.Getter;

@Getter
public class DeleteFile implements AbstractMessage{



    private  String fileName;

    public DeleteFile(String fileName) {
        this.fileName = fileName;
    }



//    public String getFileName() {
//        return fileName;
//    }

    @Override
    public MessageType getMessageType() {

        return MessageType.FILE_DELETE;
    }

}
