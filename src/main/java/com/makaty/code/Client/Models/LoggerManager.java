package com.makaty.code.Client.Models;

import com.makaty.code.Client.Loggers.ClientLogger;

import java.util.ArrayList;

public class LoggerManager {

    ArrayList<ClientLogger> loggers;
    private static LoggerManager instance;

    private LoggerManager() {
        loggers = new ArrayList<>();
    }

    private void notifyInfo(String message) {
        for(ClientLogger logger : loggers) {
            logger.info(message);
        }
    }

    private void notifyWarn(String message) {
        for(ClientLogger logger : loggers) {
            logger.warn(message);
        }
    }

    private void notifyError(String message) {
        for(ClientLogger logger : loggers) {
            logger.error(message);
        }
    }

    private void notifyRemote(String message) {
        for(ClientLogger logger : loggers) {
            logger.RemoteLog(message);
        }
    }

    public static LoggerManager getInstance() {
        if(instance == null) {
            instance = new LoggerManager();
        }
        return instance;
    }

    public void addLogger(ClientLogger logger) {
        loggers.add(logger);
    }

    public void info(String message) {
        notifyInfo(message);
    }

    public void warn(String message) {
        notifyWarn(message);
    }

    public void error(String message) {
        notifyError(message);
    }

    public void RemoteInfo(String message) {
        notifyRemote(message);
    }


}
