package com.makaty.code.Common.Packets.Communication;

import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Client.Models.Reply;

public class ReplyPacket implements Packet {

    private Reply reply;

    public ReplyPacket(Reply reply) {
        this.reply = reply;
    }



    @Override
    public PacketType getType() {
        return PacketType.REPLY;
    }

    public Reply getReply() {
        return reply;
    }


}