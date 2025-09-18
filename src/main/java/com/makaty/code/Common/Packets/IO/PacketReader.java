package com.makaty.code.Common.Packets.IO;


import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;

import java.nio.channels.SocketChannel;
/*
* this interface have only one function which reads the packet of type T
* now each packetReader will have a way to read it's packet type
* */

@FunctionalInterface
public interface PacketReader {
    Packet read(SocketChannel commandSocketChannel) throws RemoteDisconnectionException;
}
