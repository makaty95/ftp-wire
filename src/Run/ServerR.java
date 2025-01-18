package Run;

import Common.Utility;
import Server.Server;
import java.util.Scanner;

import java.net.InetSocketAddress;

public class ServerR {
    public static boolean debug = true;
    public static void main(String[] args) {


        try {

            if(debug) { // for development
                Utility.initializeCommands();
                Server server = new Server(new InetSocketAddress("127.12.12.12", 2121).getAddress(), 2121);
                server.start();
            } else {
                Scanner scanner = new Scanner(System.in);

                Utility.initializeCommands();

                String message = "[1] Enter custom IP and Port\n[2] Continue with my machine private network";
                String in;
                Server server = null;
                do {
                    System.out.println(message);
                    System.out.print(">>");
                    in = scanner.nextLine();
                }while(in.isEmpty() || in.isBlank() || (!in.equals("1") && !in.equals("2")));

                if(in.equals("1")) {
                    String IP, Port;
                    boolean retry;
                    do {
                        retry = false;
                        // get IP
                        do{
                            System.out.print("Enter your IP: ");
                            IP = scanner.nextLine();
                        }while(IP.isEmpty() || IP.isBlank());

                        // get Port
                        do{
                            System.out.print("Enter your Port: ");
                            Port = scanner.nextLine();
                        }while(Port.isEmpty() || Port.isBlank());

                        // initialize
                        try{
                            server = new Server(new InetSocketAddress(IP, Integer.parseInt(Port)).getAddress(), Integer.parseInt(Port));
                        }catch(NumberFormatException e) {
                            System.out.println("The port is incorrect");
                            retry = true;
                        } catch(IllegalArgumentException | SecurityException e) {
                            System.out.println("The given IP and port are illegal");
                            retry = true;
                        }
                    }while(retry);

                } else {
                    // the server will have the same address as the machine, if the machine have private network
                    // address = 192.168.1.12 the server will have that.

                    server = new Server();
                }
                server.start();
            }




        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Can't start the server. the IP and port may be incorrect!");
        }

    }
}
