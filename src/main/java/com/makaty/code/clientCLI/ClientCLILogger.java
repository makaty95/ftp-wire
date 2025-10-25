package com.makaty.code.clientCLI;

import com.makaty.code.Client.Loggers.ClientLogger;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;


public class ClientCLILogger implements ClientLogger {

    // ANSI color codes
    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[94m";
    private static final String GREEN = "\u001B[32m";
    private static final String GRAY = "\u001B[90m";






    private final LineReader reader;
    private final Terminal terminal;

    public ClientCLILogger(LineReader reader, Terminal terminal) {
        this.terminal = terminal;
        this.reader = reader;
    }

    @Override
    synchronized public void info(String message) {

        terminal.writer().print("\r" + GRAY + "[-]: " + message + RESET + "\n");
        terminal.flush();

        //reader.printAbove("[-]: " + message);
    }

    @Override
    synchronized public void fileProgress(double progress) {

        terminal.writer().print("\r" + BLUE + "[-]: " + String.format("Progress: %.2f%%", progress) + RESET);
        terminal.flush();

        //reader.printAbove("[-]: " + message);
    }



    @Override
    synchronized public void warn(String message) {
//        reader.printAbove( YELLOW + "[WARNING]: " + message + RESET);
        terminal.writer().print("\r" + YELLOW + "[WARNING]: " + message + RESET + "\n");
        terminal.flush();
    }

    @Override
    synchronized public void error(String message) {
//        reader.printAbove( RED + "[ERROR]: " + message + RESET);
        terminal.writer().print("\r" + RED + "[ERROR]: " + message + RESET + "\n");
        terminal.flush();
    }

    @Override
    synchronized public void RemoteLog(String message) {
        terminal.writer().print("\r" + GREEN + message + RESET + "\n");
        terminal.flush();

//        reader.printAbove(  GREEN + message + RESET);
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