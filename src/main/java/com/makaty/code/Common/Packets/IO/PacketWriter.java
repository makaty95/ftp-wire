package com.makaty.code.Common.Packets.IO;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface PacketWriter {
    Void write(Packet packet, SocketChannel channel) throws RemoteDisconnectionException;
}