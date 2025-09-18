package com.makaty.code.Common.Packets.HansShaking;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Types.PacketType;

public class DonePacket implements Packet {
    private final boolean pairSuccess;

    public DonePacket(boolean pairSuccess) {
        this.pairSuccess = pairSuccess;
    }

    public boolean isPairSuccess() {
        return pairSuccess;
    }

    @Override
    public PacketType getType() {
        return PacketType.PAIR_DONE;
    }
}