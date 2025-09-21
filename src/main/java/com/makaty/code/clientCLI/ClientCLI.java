package com.makaty.code.clientCLI;

import com.makaty.code.Client.Client;
import com.makaty.code.Common.Models.Command;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;


public class ClientCLI {

    private final Client client;
    private final Terminal terminal;
    private final LineReader reader;
    boolean running;



    public ClientCLI() throws IOException {
        terminal = TerminalBuilder.builder().jna(false).jansi(true).system(true).build();  // force Jansi mode.system(true).build();
        reader = LineReaderBuilder.builder().terminal(terminal).build();

        client = new Client();
        client.addLogger(new ClientCLILogger(terminal));

        System.out.println("Terminal type: " + terminal.getType());
    }


    private void sendCommand(Command command) throws IOException {
        client.sendCommand(command);
    }


    private Command writeCommand() {
        String in;
        String userName = client.getUserName();
        do {

            terminal.writer().printf("\r(%s)> ", userName);
            terminal.writer().flush();

            in = reader.readLine();


        }while(in.isBlank() && client.isConnected());



        String[] splits = in.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) ret.addParam(splits[i]);

        return ret;
    }

    private String getCLIInfo(String message, String flag, boolean empty) {
        String buffer;
        do{
            buffer = reader.readLine(String.format("%s %s", message, flag));
        }while(buffer.isBlank() && !empty);

        return buffer;
    }


    private void setInfo() {

        String flag = ">> ";
        String message = "[1] Enter custom IP and Port\n[2] Continue with my machine private network\n";

        String S_IP, S_PORT;

        // server ip
        message = "Enter Server IP";
        S_IP = getCLIInfo(message, flag, false);

        // server port
        message = "Enter Server Port";
        S_PORT = getCLIInfo(message, flag, false);

        client.setRemoteHost(S_IP, Integer.parseInt(S_PORT));

    }




    public void fire() {

        running = true;

        while(running) {


            if(client.isConnected()) {
                // write command
                Command command = writeCommand();

                // send command to server
                try {
                    sendCommand(command);
                } catch (IOException e) {
                    terminal.writer().println("Failed to send command to remote!");
                    terminal.writer().flush();
                }
            } else {
                terminal.writer().println("-- Offline mode --");
                terminal.writer().flush();

                //TODO: handle a local command
                new OfflineClientCLI(terminal, client).start();

            }

        }


    }



}
