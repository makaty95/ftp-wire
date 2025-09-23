package com.makaty.code.Client.Models;

import com.makaty.code.Client.Controllers.CommandController;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Packets.HansShaking.DonePacket;
import com.makaty.code.Common.Packets.HansShaking.HelloPacket;
import com.makaty.code.Common.Packets.HansShaking.PairPacket;
import com.makaty.code.Common.Packets.HansShaking.WelcomePacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.rmi.ConnectIOException;

public class ConnectionManager {
    private SocketChannel commandSocketChannel, dataSocketChannel;
    private InetSocketAddress remoteCommandAddress, remoteDataAddress;
    private InetSocketAddress localCommandAddress, localDataAddress;
    private String sessionId;
    private String workingDir;
    private static ConnectionManager instance;

    private ConnectionManager() {
        workingDir = "\\";
    }

    public static ConnectionManager getInstance() {
        if(instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void initConnection() throws IOException {
        handshake();
        CommandController.getInstance().startReceiver();
    }

    private void handshake() throws IOException {

        //TODO: remove ClientConfig.LOCAL_PORT because it won't be used
        localCommandAddress = new InetSocketAddress(ClientConfig.LOCAL_HOST, 0);
        localDataAddress = new InetSocketAddress(ClientConfig.LOCAL_HOST, 0);
        remoteCommandAddress = new InetSocketAddress(ClientConfig.REMOTE_HOST, ClientConfig.REMOTE_PORT);

        try {

            // [1] Connect to command socket
            /// [1.1] binding command channel address
            commandSocketChannel = SocketChannel.open();
            commandSocketChannel.bind(localCommandAddress);

            /// [1.2] connect to remote
            commandSocketChannel.connect(remoteCommandAddress);

            // [2] Send hello packet
            String userName = ClientConfig.USER_NAME;
            HelloPacket packet = new HelloPacket(userName);
            PacketType.HELLO.getWriter().write(packet, commandSocketChannel);

            // [3] Get welcome packet
            WelcomePacket welcomePacket = (WelcomePacket) PacketType.WELCOME.getReader().read(commandSocketChannel);
            sessionId = welcomePacket.getSessionId();

            // [4] connect to server data socket

            /// [4.1] extracting remote address
            int dataSocketPort = welcomePacket.getDataPairRequestPort();
            String dataHostName = welcomePacket.getDataHostName();
            remoteDataAddress = new InetSocketAddress(dataHostName, dataSocketPort);

            /// [4.2] binding data channel address
            dataSocketChannel = SocketChannel.open();
            dataSocketChannel.bind(localDataAddress);

            /// [4.3] connect to remote
            dataSocketChannel.connect(remoteDataAddress);


            // [5] Send pair packet
            PairPacket pairPacket = new PairPacket(sessionId);
            PacketType.PAIR_REQUEST.getWriter().write(pairPacket, dataSocketChannel);


            // [6] Get done packet
            DonePacket donePacket = (DonePacket) PacketType.PAIR_DONE.getReader().read(commandSocketChannel);
            if(!donePacket.isPairSuccess()) throw new IOException();

        } catch (IOException e) {
            throw new IOException("Failed to connect to the remote, please verify remote info.\n");
        }

    }

    public String getSessionId(){return sessionId;}

    public boolean isConnected() {return sessionId != null;}

    public void terminateConnection() throws ConnectIOException {
        try {
            commandSocketChannel.close();
            dataSocketChannel.close();
            sessionId = null;
            instance = null;

            // stop the command controller
            CommandController.getInstance().terminate();

            LoggerManager.getInstance().info("Connection terminated.\n");
        } catch (IOException e) {
            throw new ConnectIOException("Something happened during connection termination\n");
        }
    }

    public SocketChannel getCommandSocketChannel() {
        return commandSocketChannel;
    }

    public SocketChannel getDataSocketChannel() {
        return dataSocketChannel;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String dir) {
        workingDir = dir;
    }
}
