package Server;

import java.io.IOException;
import java.net.*;

public class Server {
    private final InetAddress serverAddress;
    private final int serverPort;

    public Server(InetAddress ipAddress, int port) {
        this.isServerRunning = true;
        this.serverAddress = ipAddress;
        this.serverPort = port;
    }

    // as a default thing ip will be the local machine ip
    public Server() throws UnknownHostException {
        this(InetAddress.getLocalHost(), 2121);
    }

    boolean isServerRunning;
    public void start() throws IOException {

        // start the server
        ServerSocket serverSocket = new ServerSocket(serverPort, 50, serverAddress);

        while(isServerRunning) {
            System.out.println("[" +serverSocket.getInetAddress().getHostAddress() + "] Listening for clients...");

            // accept client connection
            Socket commandSocket = serverSocket.accept();
            Socket connectionSocket = serverSocket.accept();

            System.out.println("[CMD:" + commandSocket.getRemoteSocketAddress() + " - CON:"
                    + connectionSocket.getRemoteSocketAddress() + "] " + "Connected.");

            new ServerServiceThread(commandSocket, connectionSocket).start();


        }



    }




}
