package Run;

import Client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;


public class ClientR {

    public static void main(String[] args) throws IOException {

        Client client = new Client(
                new InetSocketAddress("127.1.1.1", 2121).getAddress(), // server ip
                2121,                                                                // server port
                new InetSocketAddress("127.43.5.2", 7878).getAddress(),// client ip
                7878                                                                 // client port
        );
        client.connect();
    }
}
