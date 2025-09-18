package com.makaty.code.Server.Models;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class UserConnection {
    private SocketChannel commandSocketChannel;
    private SocketChannel dataSocketChannel;

    public SocketChannel getCommandSocketChannel () {return this.commandSocketChannel;}
    public SocketChannel getDataSocketChannel() {return dataSocketChannel;}

    public void setCommandSocketChannel(SocketChannel commandSocketChannel) {
        this.commandSocketChannel = commandSocketChannel;
    }

    public void setDataSocketChannel(SocketChannel dataSocketChannel) {
        this.dataSocketChannel = dataSocketChannel;
    }

    public void close() throws IOException {
        if(commandSocketChannel != null && !commandSocketChannel.socket().isClosed()) commandSocketChannel.close();
        if(dataSocketChannel != null && !dataSocketChannel.socket().isClosed()) dataSocketChannel.close();
    }
}
