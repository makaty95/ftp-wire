package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.CommandSender;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.ResponseReceiver;
import com.makaty.code.Common.Models.Command;

import java.rmi.ConnectIOException;

public class CommandController {

    private final CommandSender commandSender;
    private ResponseReceiver responseReceiver;
    private static CommandController instance;

    private CommandController() {
        commandSender = new CommandSender();
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
        commandSender.sendCommand(command);
    }


    public void terminate() throws ConnectIOException {
        responseReceiver.terminate();
    }
}
