package com.makaty.code.Client.Controllers;

import com.makaty.code.Client.Models.CommandSender;
import com.makaty.code.Client.Models.ResponseReceiver;
import com.makaty.code.Common.Models.Command;

import java.rmi.ConnectIOException;

public class CommandController {

    private final CommandSender commandSender;
    private final ResponseReceiver responseReceiver;
    private static CommandController instance;

    private CommandController() {
        responseReceiver = new ResponseReceiver();
        commandSender = new CommandSender();
    }

    public static CommandController getInstance() {
        if(instance == null) {
            instance = new CommandController();
        }
        return instance;
    }


    public void startReceiver() {
        responseReceiver.start();
    }

    public void sendCommand(Command command) {
        commandSender.sendCommand(command);
    }


    public void terminate() throws ConnectIOException {
        responseReceiver.terminate();
    }
}
