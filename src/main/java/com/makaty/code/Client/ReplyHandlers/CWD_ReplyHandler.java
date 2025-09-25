package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.Reply;

public class CWD_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {

        String relativePath = reply.getStrings().get(0);

        //TODO: remove absolute path.
        String absolutePath = reply.getStrings().get(1);

        ConnectionManager.getInstance().setWorkingDir(relativePath);
    }
}
