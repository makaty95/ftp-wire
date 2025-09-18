package com.makaty.code.Server.Handshaking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.ClientProfile;
import com.makaty.code.Common.Models.Status;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Session {

    @JsonIgnore
    private final ClientProfile clientProfile;

    private final String sessionId;
    private boolean authenticated;
    private boolean closed;
    private Status status;

    public Session(String id, String userName, SocketChannel commandSocket) {

        // setting profile
        this.clientProfile = new ClientProfile();
        this.clientProfile.setUserName(userName);
        this.clientProfile.setCommandSocket(commandSocket);


        this.sessionId = id;
        this.status = Status.PENDING;
        this.closed = false;
    }

    public String getClientName() {
        return clientProfile.getUserName();
    }

    public ClientProfile getClientProfile(){return clientProfile;}

    public Status getStatus(){return this.status;}

    public String getSessionId() {
        return sessionId;
    }


    @JsonIgnore
    public SocketChannel getCommandSocketChannel() {
        return clientProfile.getCommandSocketChannel();
    }

    @JsonIgnore
    public SocketChannel getDataSocketChannel() {
        return clientProfile.getDataSocketChannel();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void attachDataSocket(SocketChannel dataSocketChannel) {
        clientProfile.setDataSocketChannel(dataSocketChannel);
    }

    public void close() {
        if(closed) return;
        try {
            clientProfile.closeConnection();
            closed = true;
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: add logs here
        }
    }


    public void completeRegistration(SocketChannel dataSocketChannel) {
        this.clientProfile.setDataSocketChannel(dataSocketChannel);
        this.status = Status.ACTIVE;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert entire object to JSON
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            Server.serverLogger.warn("An instance of Session couldn't be parsed to JSON.");
        }
        return "FAILED PARSING";
    }
}
