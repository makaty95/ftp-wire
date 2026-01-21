package com.makaty.code.clientCLI;

import com.makaty.code.Client.Client;
import com.makaty.code.Common.Exceptions.CommandFormatException;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Exceptions.NoCommandWithSpecifiedHeaderException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OfflineClientCLI {

    private final Terminal terminal;
    private final Client client;
    private final LineReader reader;
    private boolean running;


    public OfflineClientCLI(Terminal terminal, Client client) {
        this.terminal = terminal;
        this.client = client;
        this.reader = LineReaderBuilder.builder().terminal(terminal).build();

    }


    private Command writeCommand() {
        String in;
        do {

            // build dynamic info
            String user = System.getProperty("user.name");
            String dir = System.getProperty("user.dir");
            String host = null;

            try {
                host = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                //TODO: add logs here.
                throw new RuntimeException(e);
            }

            // build colorized prompt
            AttributedStringBuilder promptBuilder = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                    .append(user + "@" + host)
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA))
                    .append("::")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                    .append(dir)
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                    .append("\n-{> ");

            in = reader.readLine(promptBuilder.toAnsi());

        }while(in.isBlank());



        String[] splits = in.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) ret.addParam(splits[i]);

        return ret;
    }

    private void terminate(){running = false;}

    private void executeCommand(Command command) {
        try {
            OfflineCommandType type = OfflineCommandType.validateCommand(command);
            type.getCommandHandler().handle(command, client, terminal);
            if(type == OfflineCommandType.INIT_CONNECTION) {this.terminate();}
        } catch (NoCommandWithSpecifiedHeaderException | CommandFormatException e) {
            terminal.writer().println(e.getMessage());
            terminal.writer().flush();
        }

    }

    public void start() {
        running = true;
        while(running) {
            Command command = writeCommand();
            executeCommand(command);
        }
    }
}
