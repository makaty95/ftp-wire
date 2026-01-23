package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Controllers.DataController;
import com.makaty.code.Client.Models.Reply;


public class UPLOAD_ACK_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {

        // handle the green flag to upload file
        String filePath = reply.getStrings().get(0); // path to a file to be uploaded
        DataController.getInstance().sendFile(filePath);
    }
}
