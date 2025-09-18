package com.makaty.code.Client;

import com.makaty.code.Client.Controllers.CommandController;
import com.makaty.code.Client.Controllers.DataController;
import com.makaty.code.Client.Loggers.ClientLogger;
import com.makaty.code.Client.Models.ClientConfig;
import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Models.Status;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectIOException;


public class Client {


    public Client() {}

    // Helper functions
    private boolean isValidHost(String host) {
        try {
            InetAddress.getByName(host);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    // User info
    public void addLogger(ClientLogger logger) {
        LoggerManager.getInstance().addLogger(logger);
    }

    public void setLocalHost(String host, int port) {

        if(isConnected()) {
            LoggerManager.getInstance().error("Modification of local host requires termination of the connection first.\n");
            return;
        }

        // validate port
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        // validate host
        if (!isValidHost(host)) {
            throw new IllegalArgumentException("Invalid host: " + host);
        }

        ClientConfig.HOST_NAME = host;
        ClientConfig.PORT = port;
    }

    public void setUserName(String userName) {
        if(userName.isEmpty()) {
            throw new IllegalArgumentException("Provided username is empty");
        }

        ClientConfig.USER_NAME = userName;
    }

    public String getUserName() {
        return ClientConfig.USER_NAME;
    }



    // Server interaction
    public void sendCommand(Command command) {
        CommandController.getInstance().sendCommand(command);
    }

    public void sendFile(String filePath) {
        DataController.getInstance().sendFile(filePath);
    }



    // Connection configuration
    public void initConnection() {
        try {
            Status state = ConnectionManager.getInstance().initConnection();
            //TODO: use state value
            CommandController.getInstance().startReceiver();
        } catch (IOException e) {
            //TODO: add logs here
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        return ConnectionManager.getInstance().isConnected();
    }

    public void setRemoteHost(String host, int port) {
        if(isConnected()) {
            LoggerManager.getInstance().error("Modification of remote host requires termination of the connection first.\n");
            return;
        }

        // validate port
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535");
        }

        // validate host
        if (!isValidHost(host)) {
            throw new IllegalArgumentException("Invalid host: " + host);
        }

        ConnectionManager.getInstance().setRemoteCommandSocketAddress(new InetSocketAddress(host, port));
    }

    public void terminateConnection() throws ConnectIOException {
        ConnectionManager.getInstance().terminateConnection();
    }


}
