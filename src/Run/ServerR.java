package Run;

import Common.Utility;
import Server.Server;

import java.net.InetSocketAddress;

public class ServerR {
    public static void main(String[] args) {
        try {
            Utility.initializeCommands();
            Server server = new Server(new InetSocketAddress("127.1.1.1", 2121).getAddress(), 2121);
            server.start();
        } catch(Exception e) {
            System.out.println("Can't start the server.");
        }

    }
}
