package com.makaty.code.clientCLI;

import com.makaty.code.Client.Loggers.ClientLogger;
import org.jline.terminal.Terminal;

public class ClientCLILogger implements ClientLogger {

    // ANSI color codes
    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";

    Terminal terminal;
    public ClientCLILogger(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void info(String message) {
        terminal.writer().printf("\r[-]: %s", message);
        terminal.flush();
    }

    @Override
    public void warn(String message) {
        terminal.writer().printf(YELLOW + "\r[WARNING]: %s" + RESET, message);
        terminal.flush();
    }

    @Override
    public void error(String message) {
        terminal.writer().printf(RED + "\r[ERROR]: %s" + RESET, message);
        terminal.flush();
    }

    @Override
    public void RemoteLog(String message) {
        terminal.writer().printf("\r[Remote]: %s", message);
        terminal.flush();
    }
}
