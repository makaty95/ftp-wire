package com.makaty.code.Client.Models;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Packets.Communication.CommandPacket;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;


public class CommandSender {

    public CommandSender() {}

    public void sendCommand(Command command) {

        // check connection
        if(!ConnectionManager.getInstance().isConnected()) return;

        CommandPacket commandPacket = new CommandPacket(command, ConnectionManager.getInstance().getSessionId());
        try {
            PacketType.COMMAND.getWriter().write(commandPacket, ConnectionManager.getInstance().getCommandSocketChannel());
        } catch (RemoteDisconnectionException e) {
            //TODO: add logs here
            throw new RuntimeException(e);
        }
    }
}
