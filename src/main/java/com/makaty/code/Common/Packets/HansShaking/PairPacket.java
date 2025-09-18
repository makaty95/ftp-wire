package com.makaty.code.Common.Packets.HansShaking;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Types.PacketType;

public class PairPacket implements Packet {
    private final String sessionId;

    public PairPacket(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public PacketType getType() {
        return PacketType.PAIR_REQUEST;
    }

    public String getSessionId() {
        return sessionId;
    }

}
