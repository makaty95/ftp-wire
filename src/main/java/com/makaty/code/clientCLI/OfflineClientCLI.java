package com.makaty.code.clientCLI;

import com.makaty.code.Client.Client;
import com.makaty.code.Common.Exceptions.CommandFormatException;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Server.Exceptions.NoCommandWithSpecifiedHeaderException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

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
        String userName = client.getUserName();
        do {

            terminal.writer().printf("\r(%s)> ", userName);
            terminal.writer().flush();

            in = reader.readLine();

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
