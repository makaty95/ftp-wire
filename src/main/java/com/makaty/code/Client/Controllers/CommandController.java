package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.CommandSender;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.ResponseReceiver;
import com.makaty.code.Common.Models.Command;

import java.rmi.ConnectIOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class CommandController {

    private final CommandSender commandSender;
    private ResponseReceiver responseReceiver;
    private static CommandController instance;

    private CommandController() {
        this(new CommandSender());
    }

    // Another constructor for testability
    public CommandController(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    private final ConcurrentHashMap<String,Command> pendingCommands = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String,Command> getPendingCommands() {
        return pendingCommands;
    }

    public static CommandController getInstance() {
        if(instance == null) {
            instance = new CommandController();
        }
        return instance;
    }
    public void startReceiver() {
        if (responseReceiver == null || !responseReceiver.isAlive()) {
            responseReceiver = new ResponseReceiver();
            responseReceiver.start();
        } else {
            LoggerManager.getInstance().warn("ResponseReceiver already running!");
        }
    }

    public void sendCommand(Command command) {
        CountDownLatch latch = new CountDownLatch(1);//create a latch for every command for isolation
        command.setLatch(latch);

        commandSender.sendCommand(command);

        pendingCommands.put(command.getCommandId(),command);

        try{
            latch.await();
        }catch(InterruptedException e ){
            Thread.currentThread().interrupt();
        }finally{
            pendingCommands.remove(command.getCommandId());
        }
    }

    //release the latch when response received
    public void signalResponseReceived(Command command){
        if(command.getLatch()!=null) {
            command.getLatch().countDown();
        }

    }

    public void terminate() throws ConnectIOException {
        responseReceiver.terminate();
    }
}
