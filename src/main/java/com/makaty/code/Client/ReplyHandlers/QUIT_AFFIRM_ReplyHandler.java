package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.Reply;

import java.rmi.ConnectIOException;

public class QUIT_AFFIRM_ReplyHandler implements ReplyHandler {

    @Override
    public void handle(Reply reply) {


        String sessionId = reply.getStrings().get(0); // sessionId is not used (for now)
        if(ConnectionManager.getInstance().isConnected()) {
            try {
                ConnectionManager.getInstance().terminateConnection();
            } catch (ConnectIOException e) {
                //TODO: add logs here.
                throw new RuntimeException(e);
            }
        }

    }
}