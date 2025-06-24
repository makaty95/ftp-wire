package Server;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import Common.Status;

public class Server {
    private final InetSocketAddress serverAddress;
    private ServerService serverService;

    public Server(InetSocketAddress ipAddress) throws IOException {
        serverService = new ServerService();
        this.isServerRunning = true;
        this.serverAddress = ipAddress;

        
    }

    // as a default thing ip will be the local machine ip
    public Server() throws UnknownHostException, IOException {
        this(new InetSocketAddress("localhost", 2121));
    }

    boolean isServerRunning;
    public void start() throws IOException {

        // start the server
        System.out.println("Starting server...");
        //ServerSocket serverSocket = new ServerSocket(serverPort, 50, serverAddress);

        
        // new InetServerA
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(serverAddress);
        
        // starting the service
        serverService.start();

        while(isServerRunning) {
            System.out.println("[" +serverSocketChannel.socket().getInetAddress().getHostAddress() + "] Listening for clients...");

            /////////////////////////////////////////////////////////////
            // accept client connection
            SocketChannel commandSocket_ch = serverSocketChannel.accept();
            SocketChannel connectionSocket_ch = serverSocketChannel.accept();
            
            // Socket commandSocket = serverSocket.accept();
            // Socket connectionSocket = serverSocket.accept();
            ////////////////////////////////////////////////////////////
            System.out.println("[CMD:" + commandSocket_ch.socket().getRemoteSocketAddress() + " - CON:"
                    + connectionSocket_ch.socket().getRemoteSocketAddress() + "] " + "Connected.");

            //new ServerServiceThread(commandSocket, connectionSocket).start();

            // send a verification message to the client
            serverService.showMessage(serverService.sendStatusMessage(Status.CONNECTION_ESTABLISHED, commandSocket_ch));
            
            // NonBlocking IO
            commandSocket_ch.configureBlocking(false);
            commandSocket_ch.register(this.serverService.commandSelector, SelectionKey.OP_READ);
            
            // wakup
            this.serverService.commandSelector.wakeup();
            
            // add thee link between the 2 hashes.
            this.serverService.addToHashStore(commandSocket_ch, connectionSocket_ch);


            System.out.println("Entry pushed in the NonBlocking IO");
            System.out.println("selector size: " + serverService.commandSelector.keys().size());


            /* Map:
             * commandSocketHashCode -> connectionSocketHashCode
             */

            


        }



    }




}
