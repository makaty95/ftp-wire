package com.makaty.code.Server.SocketAcceptors;

import com.makaty.code.Server.Handshaking.HandShakeManager;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.ServerConfig;
import com.makaty.code.Server.Models.TaskDispatcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class DataSocketAcceptor extends Thread {
    private ServerSocketChannel serverSocketChannel;
    private boolean working;
    private final InetSocketAddress address;

    public DataSocketAcceptor() {
        this.address = new InetSocketAddress(ServerConfig.DATA_HOST, ServerConfig.DATA_PORT);
        working = true;
    }

    public boolean isWorking() {
        return working;
    }

    public int getPort() {
        return ServerConfig.DATA_PORT;
    }

    public void terminate() {
        this.working = false;
        try {
            if(serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            //TODO: add logs here
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {

        try{serverSocketChannel = ServerSocketChannel.open();} catch (Exception e) {
            Server.serverLogger.error("Could not open a server socket channel.");
            Server.serverLogger.info("DataSocketAcceptor terminated.");
            return;
        }

        try{serverSocketChannel.bind(this.address);} catch (AlreadyBoundException e) {
            Server.serverLogger.error("Could not bind address to the server. socket is already bound.");
            Server.serverLogger.info("DataSocketAcceptor terminated.");
            return;
        } catch (SecurityException e) {
            Server.serverLogger.error("Could not bind address to the server. Some security manager denies it.");
            Server.serverLogger.info("DataSocketAcceptor terminated.");
            return;
        } catch (Exception e) {
            Server.serverLogger.error("Could not bind address to the server. Some error happened.");
            Server.serverLogger.info("DataSocketAcceptor terminated.");
            return;
        }

        try {serverSocketChannel.configureBlocking(true);} catch(Exception e) {
            Server.serverLogger.warn("Could not configure the blocking mode of serverSocketChannel, channel is already closed!");
        }

        Server.serverLogger.info("DataAcceptor instance is up and running.");
        while(working) {
            try {
                /*
                * 1) get the pair packet
                * 2) verify if the session exists
                * 3) send back the done packet
                *
                * */

                try {
                    SocketChannel dataSocketChannel = serverSocketChannel.accept();

                    if(dataSocketChannel != null) {
                        Server.serverLogger.info("data socket accepted");
                        TaskDispatcher.getInstance().submitAsyncTask(() -> new HandShakeManager().completeHandShake(dataSocketChannel));

                    }
                } catch (ClosedChannelException e) {/*this is intended, to fully close the socket and the thread*/}

            } catch (IOException e) {
                Server.serverLogger.warn("Some error occurred during accepting data socket channels");
            }
        }


    }
}
