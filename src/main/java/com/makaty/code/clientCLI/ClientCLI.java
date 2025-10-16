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
        client.addLogger(new ClientCLILogger(reader));

        System.out.println("Terminal type: " + terminal.getType());
    }


    private void sendCommand(Command command) throws IOException {
        client.sendCommand(command);
    }


    private Command writeCommand() {
        String in;

        do {
            String userName = client.getUserName();
            String prompt = String.format("(%s@%s)> ", userName, client.getWorkingDir());
            in = reader.readLine(prompt); // JLine handles the prompt
        } while (in.isBlank() && client.isConnected());

        String[] splits = in.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) ret.addParam(splits[i]);

        return ret;
    }

    public void fire() {

        running = true;
        String banner = """

                           ___  __                                                   \s
                         /'___\\/\\ \\__                               __               \s
                        /\\ \\__/\\ \\ ,_\\  _____            __  __  __/\\_\\  _ __    __  \s
                        \\ \\ ,__\\\\ \\ \\/ /\\ '__`\\  _______/\\ \\/\\ \\/\\ \\/\\ \\/\\`'__\\/'__`\\\s
                         \\ \\ \\_/ \\ \\ \\_\\ \\ \\L\\ \\/\\______\\ \\ \\_/ \\_/ \\ \\ \\ \\ \\//\\  __/\s
                          \\ \\_\\   \\ \\__\\\\ \\ ,__/\\/______/\\ \\___x___/'\\ \\_\\ \\_\\\\ \\____\\
                           \\/_/    \\/__/ \\ \\ \\/           \\/__//__/   \\/_/\\/_/ \\/____/
                                          \\ \\_\\                                      \s
                                           \\/_/                                      \s
                        
                        """;

        terminal.writer().write(banner);
        terminal.flush();


        while(running) {


            if(client.isConnected()) {
                terminal.writer().println("==================================================");
                terminal.writer().println("                  (ONLINE MODE)                   ");
                terminal.writer().println("==================================================");
                terminal.writer().flush();

                while(client.isConnected()) {
                    // write command
                    Command command = writeCommand();

                    // send command to server
                    try {
                        sendCommand(command);
                    } catch (IOException e) {
                        terminal.writer().println("Failed to send command to remote!");
                        terminal.writer().flush();
                    }
                }
            } else {


                terminal.writer().println("==================================================");
                terminal.writer().println("                  (OFFLINE MODE)                  ");
                terminal.writer().println("==================================================");
                terminal.writer().flush();

                //TODO: handle a local command
                new OfflineClientCLI(terminal, client).start();

            }

        }


    }



}
