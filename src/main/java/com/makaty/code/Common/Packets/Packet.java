package com.makaty.code.Common.Packets;

import com.makaty.code.Common.Types.PacketType;

import java.io.Serializable;

public interface Packet extends Serializable {
    PacketType getType();
}
