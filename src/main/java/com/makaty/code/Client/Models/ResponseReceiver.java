package com.makaty.code.Client.Models;

import com.makaty.code.Client.Controllers.CommandController;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;
import java.rmi.ConnectIOException;

//TODO: Implement Unit tests
public class ResponseReceiver extends Thread {

    private volatile boolean running = false;


    public void run() {
        running = true;
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    ReplyPacket replyPacket = (ReplyPacket) PacketType.REPLY
                            .getReader()
                            .read(ConnectionManager.getInstance().getCommandSocketChannel());

                    replyPacket.getReply().getReplyType().getReplyHandler().handle(replyPacket.getReply());
                    String commandId = replyPacket.getReply().getCommandId();

                    if(commandId != null) {
                        Command command= CommandController.getInstance().getPendingCommands().get(commandId);
                        if(command != null) {
                            CommandController.getInstance().signalResponseReceived(command);
                        } else {
                            LoggerManager.getInstance().warn("Command with the following Id not found : "+commandId);
                        }
                    }
                } catch (RemoteDisconnectionException e) {
                    LoggerManager.getInstance().info("Remote disconnected");
                    terminate(); // close the connection
                }
            }
        } catch (Exception e) {
            LoggerManager.getInstance().error("ResponseReceiver crashed: " + e.getMessage());
        } finally {
            // cleanup
            try {
                ConnectionManager.getInstance().terminateConnection();
            } catch (Exception ignored) {}
        }
    }

    public void terminate() {
        running = false;
        this.interrupt(); // wake thread if stuck in blocking call
        try {
            if (ConnectionManager.getInstance().isConnected()) {
                ConnectionManager.getInstance().terminateConnection();
            }
        } catch (ConnectIOException e) {
            LoggerManager.getInstance().warn("Error closing connection: " + e.getMessage());
        }

    }
}
