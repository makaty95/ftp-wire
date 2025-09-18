package com.makaty.code.Server.Tasks;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.ClientProfile;
import com.makaty.code.Common.Models.Status;

import java.io.IOException;

public class SendPacketTask extends CommandTask {

    private final Packet packet;

    public SendPacketTask(ClientProfile clientProfile, Packet packet) {
        super(clientProfile);
        this.packet = packet;
    }

    @Override
    public Status call() {

        // sending the packet to the client
        try{packet.getType().getWriter().write(packet, clientProfile.getCommandSocketChannel());} catch (IOException e) {
            Server.serverLogger.warn(String.format("Something happened when sending a Packet to client \"%s\"", clientProfile.getUserName()));
            return Status.FAILED;
        }
        return Status.SUCCESS;
    }
}
