package com.makaty.code.Server.Loggers;

import com.makaty.code.Server.Handshaking.Session;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class ServerCLILogger implements ServerLogger {
    Terminal serverTerminal;

    // ANSI color codes
    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";

    public ServerCLILogger() {

        try {
            serverTerminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            //TODO: add logs here
            throw new RuntimeException(e);
        }
    }

    @Override
    public void warn(String message) {
        serverTerminal.writer().printf(YELLOW + "[SERVER_LOGGER] [WARNING]: %s" + RESET + "%n", message);
        serverTerminal.flush();
    }

    @Override
    public void info(String message) {
        serverTerminal.writer().printf(BLUE + "[SERVER_LOGGER] [INFO]: %s" + RESET + "%n", message);
        serverTerminal.flush();
    }

    @Override
    public void error(String message) {
        serverTerminal.writer().printf(RED + "[SERVER_LOGGER] [ERROR]: %s" + RESET + "%n", message);
        serverTerminal.flush();
    }

    @Override
    public void reportClientConnection(Session session) {
        info(String.format("[Client \"%s\" Connected]", session.getClientName()));
        info(session.toString());
        serverTerminal.flush();
    }
}
