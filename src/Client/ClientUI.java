package Client;

import Common.Command;
import Common.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class ClientUI {

    //private Client client;
    protected InputStream commandIn, connectionIn;
    protected OutputStream commandOut, connectionOut;
    public boolean isConnected;
    private Scanner scanner;

    public ClientUI(Client client) throws IOException {
        //this.client = client;
        commandIn = client.commandSocket.getInputStream();
        commandOut = client.commandSocket.getOutputStream();
        connectionIn = client.connectionSocket.getInputStream();
        connectionOut = client.connectionSocket.getOutputStream();
        isConnected = true;
        scanner = new Scanner(System.in);

    }

    private void sendCommand(Command command) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(command.getHeader());

        int n = command.getParamsCount();
        for(int i = 0; i<n; i++) {
            sb.append(" ");
            sb.append(command.getParam(i));
        }

        // send the command as a plain text.
        this.commandOut.write(sb.toString().getBytes());
    }

    private Command writeCommand() {

        String in;
        do {
            System.out.print("CMD> ");
            in = scanner.nextLine();

        }while(in.isBlank() || in.isEmpty());

        String[] splits = in.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) ret.addParam(splits[i]);

        if(ret.equals(Utility.byeCommand)) {
            isConnected = false;
        }

        return ret;
    }

    public void fire() {

        //System.out.println(commandIn.available());

        new ReplyReceiver(this).start();
        try{
            while(isConnected) {
                // write command
                Command command = writeCommand();

                // send command to server
                sendCommand(command);
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unhandled Exception in ClientUI.java - 1891");
        }

        System.out.println("You quite.");
    }



}
