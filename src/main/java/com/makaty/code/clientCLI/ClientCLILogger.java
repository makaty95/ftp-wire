package com.makaty.code.clientCLI;

import com.makaty.code.Client.Loggers.ClientLogger;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;


public class ClientCLILogger implements ClientLogger {

    // ANSI color codes
    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";






    private final LineReader reader;

    public ClientCLILogger(LineReader reader) {
        this.reader = reader;
    }

    @Override
    synchronized public void info(String message) {
        reader.printAbove("[-]: " + message);
    }

    @Override
    synchronized public void warn(String message) {
        reader.printAbove( YELLOW + "[WARNING]: " + message + RESET);
    }

    @Override
    synchronized public void error(String message) {
        reader.printAbove( RED + "[ERROR]: " + message + RESET);
    }

    @Override
    synchronized public void RemoteLog(String message) {
        reader.printAbove( GREEN + message + RESET);
    }

}


//    Terminal terminal;
//    public ClientCLILogger(Terminal terminal) {
//        this.terminal = terminal;
//    }
//
//    @Override
//    synchronized public void info(String message) {
//
//        terminal.writer().printf("\r[-]: %s", message);
//        terminal.flush();
//    }
//
//    @Override
//    synchronized public void warn(String message) {
//        terminal.writer().printf("\r" +YELLOW + "[WARNING]: %s" + RESET, message);
//        terminal.flush();
//    }
//
//    @Override
//    synchronized public void error(String message) {
//        terminal.writer().printf("\r" + RED + "[ERROR]: %s" + RESET, message);
//        terminal.flush();
//    }
//
//    @Override
//    synchronized public void RemoteLog(String message) {
//        terminal.writer().printf("\r" + BLUE + "[Remote]: %s" + RESET + "\n", message);
//        terminal.flush();
//    }

//    private final LineReader reader;
//
//    public ClientCLILogger(LineReader reader) {
//        this.reader = reader;
//    }
//
//    @Override
//    synchronized public void info(String message) {
//        reader.printAbove("\n" + "[-]: " + message);
//    }
//
//    @Override
//    synchronized public void warn(String message) {
//        reader.printAbove("\n" + YELLOW + "[WARNING]: " + message + RESET);
//    }
//
//    @Override
//    synchronized public void error(String message) {
//        reader.printAbove("\n" + RED + "[ERROR]: " + message + RESET);
//    }
//
//    @Override
//    synchronized public void RemoteLog(String message) {
//        reader.printAbove("\n" + BLUE + "[Remote]: " + message + RESET);
//    }