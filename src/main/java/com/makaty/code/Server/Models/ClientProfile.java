package com.makaty.code.Server.Models;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ClientProfile {

    private String userName;
    private final UserConnection userConnection;

    public ClientProfile() {
        userConnection = new UserConnection();
    }

    // getters
    public SocketChannel getCommandSocketChannel() {return this.userConnection.getCommandSocketChannel();}

    public SocketChannel getDataSocketChannel() {return this.userConnection.getDataSocketChannel();}

    public String getUserName() {return userName;}

    // setters
    public void setUserName(String newUserName){this.userName = newUserName;}
    public void setDataSocketChannel(SocketChannel dataSocketChannel) {
        userConnection.setDataSocketChannel(dataSocketChannel);
    }

    public void setCommandSocket(SocketChannel commandSocketChannel) {
        userConnection.setCommandSocketChannel(commandSocketChannel);
    }

    public void closeConnection() throws IOException {
        userConnection.close();
    }


}
