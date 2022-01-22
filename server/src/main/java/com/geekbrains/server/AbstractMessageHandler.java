package com.geekbrains.server;

import com.geekbrains.model.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class AbstractMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private Path currentPath;


    public AbstractMessageHandler() {
        currentPath = Paths.get("storage");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new FilesList(currentPath));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage message) throws Exception {
        log.debug("received: {}", message);
        switch (message.getMessageType()) {
            case FILE_REQUEST:
                FileRequest req = (FileRequest) message;
                ctx.writeAndFlush(new FileMessage(currentPath.resolve(req.getFileName())));
                break;
            case FILE:
                FileMessage fileMessage = (FileMessage) message;
                Files.write(
                        currentPath.resolve(fileMessage.getFileName()),
                        fileMessage.getBytes()
                );
                ctx.writeAndFlush(new FilesList(currentPath));
                break;
            case FILE_DELETE:
                DeleteFile deleteMessage = (DeleteFile) message;
                Path path = Path.of(getPath(deleteMessage.getFileName(), String.valueOf(currentPath)));
                log.debug(String.valueOf(path));
                Files.delete(path);
                ctx.writeAndFlush(new FilesList(currentPath));
                break;
            case ADD_NEW_USER:
                CreateNewUsers createNewUsersMessage = (CreateNewUsers) message;
                if(DBConnection.addNewUser(createNewUsersMessage.getName(), createNewUsersMessage.getPassword()) == 0) {
                    ctx.writeAndFlush(new CreateNewUserRequest("ok"));
                    log.debug("create new user: {}", createNewUsersMessage.getName());
                } else {
                    ctx.writeAndFlush(new CreateNewUserRequest("error"));
                    log.debug("Already exists user: {}", createNewUsersMessage.getName());
                }
                break;
            case AUTH_USER:
                AuthUser authUserMessage = (AuthUser) message;
                if ((DBConnection.getIdByLoginAndPassword(authUserMessage.getName(), authUserMessage.getPassword())) != -1){
                    ctx.writeAndFlush(new AuthRequest("ok"));
                    log.debug("connected user: {}", authUserMessage.getName());
                    currentPath = Paths.get(currentPath+"\\"+DBConnection.getIdByLoginAndPassword(authUserMessage.getName(), authUserMessage.getPassword()));
                    log.debug("new path: {}", currentPath);
                    new File(String.valueOf(currentPath)).mkdir();
                    ctx.writeAndFlush(new FilesList(currentPath));
                } else {
                    ctx.writeAndFlush(new AuthRequest("error"));
                    log.debug("error to connect");
                }
                break;
        }
    }

    private String getPath(String fileName, String folder) {
        File directory = new File(folder);

        File[] fileList = directory.listFiles();
        for (File file : fileList) {

            if(fileName.equals(file.getName())) {
                return file.getAbsolutePath();
            }


        }

        return "";
    }

}
