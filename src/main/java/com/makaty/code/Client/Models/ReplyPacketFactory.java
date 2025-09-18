package com.makaty.code.Client.Models;

import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;

@FunctionalInterface
public interface ReplyPacketFactory {
    ReplyPacket createPacket(ReplyType type, Object... args);
}
