package com.makaty.code.Server;

import com.makaty.code.Server.Loggers.ServerCLILogger;
import com.makaty.code.Server.Loggers.ServerLogger;
import com.makaty.code.Server.Models.CommandSelectorDispatcher;
import com.makaty.code.Server.Registries.SessionRegistry;
import com.makaty.code.Server.SocketAcceptors.CommandSocketAcceptor;
import com.makaty.code.Server.SocketAcceptors.DataSocketAcceptor;
import com.makaty.code.Server.Models.TaskDispatcher;

public class Server {


    private final CommandSocketAcceptor commandAcceptor;
    private final DataSocketAcceptor dataAcceptor;
    public static CommandSelectorDispatcher selectorDispatcher;
    public static SessionRegistry sessionRegistry;

    public static final ServerLogger serverLogger = new ServerCLILogger();

    public Server() {
        this.commandAcceptor = new CommandSocketAcceptor();
        this.dataAcceptor = new DataSocketAcceptor();
        selectorDispatcher = new CommandSelectorDispatcher();
        sessionRegistry = SessionRegistry.createRegistry();
    }

    public boolean isRunning() {
        return commandAcceptor.isWorking() && dataAcceptor.isWorking() && selectorDispatcher.isWorking();
    }

    public void start() {
        // Start command & data acceptors
        commandAcceptor.start();
        dataAcceptor.start();

        // Start the command dispatcher
        selectorDispatcher.start();

        Server.serverLogger.info("Server started successfully.");
    }

    public void terminate() {

        // stop receiving connection requests
        commandAcceptor.terminate();
        dataAcceptor.terminate();

        // stop executing commands
        selectorDispatcher.terminate();

        // stop dispatching tasks
        TaskDispatcher.getInstance().shutdown();

        // remove all sessions
        Server.sessionRegistry.removeAll();

        serverLogger.info("Server terminated.");
    }


}
