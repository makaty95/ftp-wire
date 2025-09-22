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
    synchronized public void info(String message) {
        
        terminal.writer().printf("\r[-]: %s", message);
        terminal.flush();
    }

    @Override
    synchronized public void warn(String message) {
        terminal.writer().printf("\r" +YELLOW + "[WARNING]: %s" + RESET, message);
        terminal.flush();
    }

    @Override
    synchronized public void error(String message) {
        terminal.writer().printf("\r" + RED + "[ERROR]: %s" + RESET, message);
        terminal.flush();
    }

    @Override
    synchronized public void RemoteLog(String message) {
        terminal.writer().printf("\r" + BLUE + "[Remote]: %s" + RESET + "\n", message);
        terminal.flush();
    }
}
