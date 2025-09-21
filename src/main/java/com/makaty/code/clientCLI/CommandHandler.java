package com.makaty.code.clientCLI;

import com.makaty.code.Client.Client;
import com.makaty.code.Common.Models.Command;
import org.jline.terminal.Terminal;

@FunctionalInterface
public interface CommandHandler {
    void handle(Command command, Client client, Terminal terminal);
}
