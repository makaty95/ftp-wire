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

public class CommandSocketAcceptor extends Thread {
    private ServerSocketChannel serverSocketChannel;
    private boolean working;
    private final InetSocketAddress address;

    public CommandSocketAcceptor() {
        this.address = new InetSocketAddress(ServerConfig.COMMAND_HOST, ServerConfig.COMMAND_PORT);

    }

    public InetSocketAddress getAddress(){return address;}

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public boolean isWorking() {
        return working;
    }

    public int getPort() {
        return ServerConfig.COMMAND_PORT;
    }

    public void terminate() {
        this.working = false;
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            //TODO: add logs here
            Server.serverLogger.warn("Some exception happened during CommandSocketAcceptor termination");
        }

    }


    @Override
    public void run() {


        working = true;


        try{serverSocketChannel = ServerSocketChannel.open();} catch (Exception e) {
            Server.serverLogger.error("Could not open a server socket channel.");
            Server.serverLogger.info("CommandSocketAcceptor terminated.");
            return;
        }

        try{serverSocketChannel.bind(this.address);} catch (AlreadyBoundException e) {
            Server.serverLogger.error("Could not bind address to the server. socket is already bound.");
            Server.serverLogger.info("CommandSocketAcceptor terminated.");
            return;
        } catch (SecurityException e) {
            Server.serverLogger.error("Could not bind address to the server. Some security manager denies it");
            Server.serverLogger.info("CommandSocketAcceptor terminated.");
            return;
        } catch (Exception e) {
            Server.serverLogger.error("Could not bind address to the server. Some error happened");
            Server.serverLogger.info("CommandSocketAcceptor terminated.");
            return;
        }

        try {serverSocketChannel.configureBlocking(true);} catch(Exception e) {
            Server.serverLogger.warn("Could not configure the blocking mode of serverSocketChannel, channel is already closed!");
        }

        Server.serverLogger.info("CommandAcceptor instance is up and running.");
        while(working) {
            try {
                /*
                 * 1) get the hello packet
                 * 2) register the client with a new session
                 * 3) send back the welcome packet to the client
                 *
                 * */


                try {
                    SocketChannel commandSocketChannel = serverSocketChannel.accept();

                    if(commandSocketChannel != null) {
                        Server.serverLogger.info("command socket accepted");
                        TaskDispatcher.getInstance().submitAsyncTask(() -> new HandShakeManager().performHandShake(commandSocketChannel));
                    }
                } catch (ClosedChannelException e) {/*this is intended, to fully close the socket and the thread*/}


            } catch (IOException e) {
                Server.serverLogger.warn("Some error occurred during accepting command socket channels");
            }
        }


    }
}
