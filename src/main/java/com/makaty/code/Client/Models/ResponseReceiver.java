package com.makaty.code.Client.Models;


import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;

import java.rmi.ConnectIOException;


public class ResponseReceiver extends Thread {

    private boolean running;

    public ResponseReceiver() {running = false;}

    public void run() {
        running = true;
        try {
            while(running) {
                ReplyPacket replyPacket = (ReplyPacket) PacketType.REPLY.getReader().read(ConnectionManager.getInstance().getCommandSocketChannel());
                replyPacket.getReply().getReplyType().getReplyHandler().handle(replyPacket.getReply());
            }
        } catch (RemoteDisconnectionException e) {
            try {
                LoggerManager.getInstance().warn("Remote server disconnected.\n");
                terminate();
            } catch (ConnectIOException ignored) {}
        }
    }

    public void terminate() throws ConnectIOException {
        running = false;
        if(ConnectionManager.getInstance().isConnected()) ConnectionManager.getInstance().terminateConnection();
    }
}
