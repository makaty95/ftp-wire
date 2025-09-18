package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Controllers.DataController;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.Reply;

import java.io.IOException;

public class FILE_INFO_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {

        long fileSize = reply.getLongs().get(0);
        String fileName = reply.getStrings().get(0);

        try {
            DataController.getInstance().receiveFile(fileName, "./storage/", fileSize);
        } catch (IOException e) {
            LoggerManager.getInstance().error("Failed to receive file.\n");
            throw new RuntimeException(e);
        }

    }




}