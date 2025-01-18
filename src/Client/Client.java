package Client;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    Socket commandSocket, connectionSocket;

    public InetAddress clientAddress;
    public InetAddress serverAddress;
    public int serverPort;
    public int clientPort;

    public Client(InetAddress serverAddress, int serverPort, InetAddress clientAddress, int clientPort) {
        this.clientPort = clientPort;
        this.clientAddress = clientAddress;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
    }

    // default ip is the machine local network address
    public Client(InetAddress serverAddress, int serverPort) throws UnknownHostException {
        this(serverAddress, serverPort, InetAddress.getLocalHost(), 7878);
    }

    public void connect() throws IOException {

        // trying to connect

        System.out.println("Connecting to to server...");
        commandSocket = new Socket(serverAddress, serverPort, clientAddress, clientPort);
        connectionSocket = new Socket(serverAddress, serverPort, clientAddress, clientPort+1);
        System.out.println("Connection established (from client side)");

        InputStream in = commandSocket.getInputStream();

        ClientUI ui = new ClientUI(this);
        ui.fire();

    }






}
