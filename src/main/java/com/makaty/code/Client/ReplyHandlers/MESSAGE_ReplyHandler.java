package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.Reply;

public class MESSAGE_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {
        String message = reply.getStrings().get(0);
        LoggerManager.getInstance().RemoteInfo(message);
    }
}
