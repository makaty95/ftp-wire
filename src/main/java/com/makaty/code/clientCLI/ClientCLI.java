package com.makaty.code.clientCLI;

import com.makaty.code.Client.Client;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Common.Models.Command;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.IOException;


public class ClientCLI {

    private final Client client;
    private final Terminal terminal;
    private final LineReader reader;
    boolean running;

    private static final String banner = """

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


    public ClientCLI() throws IOException {
        terminal = TerminalBuilder.builder().jna(false).jansi(true).system(true).build();  // force Jansi mode.system(true).build();
        reader = LineReaderBuilder.builder().terminal(terminal).build();

        client = new Client();
        client.addLogger(new ClientCLILogger(reader, terminal));

        LoggerManager.getInstance().info("Terminal type: " + terminal.getType());

    }




    private Command parseToCommand(String in) {
        String[] splits = in.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) ret.addParam(splits[i]);
        return ret;
    }

    private Command writeCommand() {
        String in;

        while(client.isConnected()) {
            // build colorized prompt
            AttributedStringBuilder promptBuilder = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN))
                    .append(client.getUserName())
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA))
                    .append("@")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                    .append(client.getWorkingDir())
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                    .append("\n-{> ");

            in = reader.readLine(promptBuilder.toAnsi()); // JLine handles the prompt

            if(!in.isBlank()) {
                return parseToCommand(in);
            }

        }

        return null;
    }

    public void fire() {

        running = true;
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
                    if(command != null) client.sendCommand(command);

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
