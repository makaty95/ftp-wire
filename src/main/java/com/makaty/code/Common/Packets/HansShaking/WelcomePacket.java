package com.makaty.code.Common.Packets.HansShaking;


import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Types.PacketType;

public class WelcomePacket implements Packet {
    private final String sessionId;
    private final String dataHostName;
    private final int dataPairRequestPort;

    public WelcomePacket(String sessionId, String dataHostName, int dataPort) {

        this.sessionId = sessionId;
        this.dataHostName = dataHostName;
        this.dataPairRequestPort = dataPort;
    }

    public int getDataPairRequestPort() {
        return dataPairRequestPort;
    }
    public String getDataHostName() {return dataHostName;}

    @Override
    public PacketType getType() {
        return PacketType.WELCOME;
    }

    public String getSessionId() {
        return sessionId;
    }
}