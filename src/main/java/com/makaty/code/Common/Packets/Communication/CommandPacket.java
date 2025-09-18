package com.makaty.code.Common.Packets.Communication;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Types.PacketType;


public class CommandPacket implements Packet {

    private final Command command;
    private final String sessionId;

    public CommandPacket(Command command, String sessionId) {
        this.command = command;
        this.sessionId = sessionId;
    }

    public Command getCommand() {
        return command;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public PacketType getType() {
        return PacketType.COMMAND;
    }


}
