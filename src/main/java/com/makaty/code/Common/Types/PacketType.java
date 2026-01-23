package com.makaty.code.Common.Types;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Client.Models.Reply;
import com.makaty.code.Common.Packets.Communication.CommandPacket;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Packets.HansShaking.DonePacket;
import com.makaty.code.Common.Packets.HansShaking.HelloPacket;
import com.makaty.code.Common.Packets.HansShaking.PairPacket;
import com.makaty.code.Common.Packets.HansShaking.WelcomePacket;
import com.makaty.code.Common.Packets.IO.PacketReader;
import com.makaty.code.Common.Packets.IO.PacketWriter;
import com.makaty.code.Common.Serialization.PacketSerializer;
import com.makaty.code.Server.Server;

import java.util.ArrayList;
import java.util.List;


public enum PacketType {

    // ----------------- Handshaking packets ----------------- //
    HELLO(0,
            channel -> {
                String username = PacketSerializer.readString(channel);
                return new HelloPacket(username);
            },
            (packet, channel) -> {
                PacketSerializer.writeString(channel, ((HelloPacket) packet).getUsername());
                return null;
            }
    ),


    WELCOME(1,
            channel -> {
                // Read sessionId
                String sessionId = PacketSerializer.readString(channel);
                // Read dataHostName
                String dataHostName = PacketSerializer.readString(channel);
                // Read dataPairRequestPort
                int dataPort = PacketSerializer.readInt(channel);
                return new WelcomePacket(sessionId, dataHostName, dataPort);
            },
            (packet, channel) -> {
                WelcomePacket welcome = (WelcomePacket) packet;
                PacketSerializer.writeString(channel, welcome.getSessionId());
                PacketSerializer.writeString(channel, welcome.getDataHostName());
                PacketSerializer.writeInt(channel, welcome.getDataPairRequestPort());
                return null;
            }
    ),

    PAIR_REQUEST(2,
            channel -> {
                String sessionId = PacketSerializer.readString(channel);
                return new PairPacket(sessionId);
            },
            (packet, channel) -> {
                PairPacket pair = (PairPacket) packet;
                PacketSerializer.writeString(channel, pair.getSessionId());
                return null;
            }
    ),

    PAIR_DONE(3,
            channel -> {
                boolean success = PacketSerializer.readInt(channel) != 0;
                return new DonePacket(success);
            },
            (packet, channel) -> {
                DonePacket done = (DonePacket) packet;
                PacketSerializer.writeInt(channel, done.isPairSuccess() ? 1 : 0);
                return null;
            }
    ),

    // ----------------- Client to Server packets ----------------- //

    COMMAND(4,
            channel -> {
                String sessionId = PacketSerializer.readString(channel);

                // command id
                String commandId = PacketSerializer.readString(channel);//Supposing the commandId is written by the other side to the channel earlier

                // header
                String header = PacketSerializer.readString(channel);

                // parameters
                ArrayList<String> params = PacketSerializer.readStrings(channel);

                // meta data
                ArrayList<String> metaData = PacketSerializer.readStrings(channel);

                Command command = new Command(header, params);
                command.setCommandId(commandId);
                command.addMetaData(metaData);
                return new CommandPacket(command, sessionId);
            },
            (packet, channel) -> {
                CommandPacket cmdPacket = (CommandPacket) packet;

                // session id
                PacketSerializer.writeString(channel, cmdPacket.getSessionId());

                // command id
                PacketSerializer.writeString(channel,cmdPacket.getCommand().getCommandId());

                // header
                PacketSerializer.writeString(channel, cmdPacket.getCommand().getHeader());

                // parameters
                PacketSerializer.writeStrings(channel, cmdPacket.getCommand().getParams());

                // meta data
                PacketSerializer.writeStrings(channel, cmdPacket.getCommand().getAllMetaData());

                return null;
            }
    ),

    // ----------------- Server to Client packets ----------------- //

    REPLY(5,//updated
            channel -> { // the reading //
                String commandId = PacketSerializer.readString(channel);
                int replyType = PacketSerializer.readInt(channel);
                List<Long> longs = PacketSerializer.readLongs(channel);
                List<String> strings = PacketSerializer.readStrings(channel);
                return new ReplyPacket(new Reply(ReplyType.values()[replyType], longs, strings,commandId));
            },
            (packet, channel) -> { // the writing //

                // casting packet
                ReplyPacket replyPacket = (ReplyPacket) packet;

                PacketSerializer.writeString(channel,replyPacket.getReply().getCommandId());
                // writing reply type
                PacketSerializer.writeInt(channel, replyPacket.getReply().getReplyType().ordinal());

                // write longs list
                PacketSerializer.writeLongs(channel, replyPacket.getReply().getLongs());

                // write strings list
                PacketSerializer.writeStrings(channel, replyPacket.getReply().getStrings());
                return null;
            }
    );


    private final int id;
    private final PacketReader reader;
    private final PacketWriter writer;

    PacketType(int id, PacketReader reader, PacketWriter writer) {
        this.id = id;
        this.reader = reader;
        this.writer = writer;
    }

    public PacketReader getReader() { return reader; }
    public PacketWriter getWriter() { return writer; }
    public int getId() { return id; }

    public static PacketType fromId(int id) {
        for (PacketType type : values()) {
            if (type.id == id) return type;
        }
        Server.serverLogger.error(String.format("Could not find a PacketType which matches id = %d", id));
        throw new IllegalArgumentException("Unknown packet id: " + id);
    }
}
