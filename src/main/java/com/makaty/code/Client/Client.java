package com.makaty.code.Client;

import com.makaty.code.Client.Controllers.CommandController;
import com.makaty.code.Client.Controllers.DataController;
import com.makaty.code.Client.Loggers.ClientLogger;
import com.makaty.code.Client.Models.ClientConfig;
import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Common.Models.Command;

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
            LoggerManager.getInstance().error("Port must be between 1 and 65535.\n");
            return;
        }

        // validate host
        if (!isValidHost(host)) {
            LoggerManager.getInstance().error(String.format("Invalid host: %s.\n", host));
            return;
        }

        ClientConfig.LOCAL_HOST = host;
        ClientConfig.LOCAL_PORT = port;
    }

    public void setUserName(String userName) {
        if(userName.isEmpty()) {
            LoggerManager.getInstance().error("Username can't be empty!\n");
            return;
        }

        ClientConfig.USER_NAME = userName;
    }

    public String getUserName() {
        return ClientConfig.USER_NAME;
    }

    public String getRemoteHost() {return ClientConfig.REMOTE_HOST;}

    public String getLocalHost() {return ClientConfig.LOCAL_HOST;}

    public int getRemotePort() {return ClientConfig.REMOTE_PORT;}

    public int getLocalPort() {return ClientConfig.LOCAL_PORT;}

    public String getWorkingDir() {
        return ConnectionManager.getInstance().getWorkingDir();
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
            ConnectionManager.getInstance().initConnection();
        } catch (IOException e) {
            LoggerManager.getInstance().error(e.getMessage());
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
            LoggerManager.getInstance().error("Port must be between 1 and 65535.\n");
            return;
        }

        // validate host
        if (!isValidHost(host)) {
            LoggerManager.getInstance().error("Invalid host: " + host + "\n");
        }

        ClientConfig.REMOTE_HOST = host;
        ClientConfig.REMOTE_PORT = port;
    }

    public void terminateConnection() throws ConnectIOException {
        ConnectionManager.getInstance().terminateConnection();
    }


}
