package com.makaty.code.Common.Packets.HansShaking;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Types.PacketType;

public class HelloPacket implements Packet {
    private final String username;

    public HelloPacket(String username) {
        this.username = username;
    }

    @Override
    public PacketType getType() {
        return PacketType.HELLO;
    }

    public String getUsername() {
        return username;
    }

}
