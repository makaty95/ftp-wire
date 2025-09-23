package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.Reply;

public class PWD_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {
        String relativePath = reply.getStrings().get(0);
        String absolutePath = reply.getStrings().get(1);

        LoggerManager.getInstance().RemoteInfo(relativePath);
    }
}
