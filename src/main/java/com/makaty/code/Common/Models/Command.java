package com.makaty.code.Common.Models;

import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Common.Exceptions.NoCommandWithSpecifiedHeaderException;
import com.makaty.code.Server.Models.Types.CommandType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Command {
    private String header;
    private ArrayList<String> params;
    private String commandId;

    ////////////////////
    ///  meta data (used to pass other info to the server for example: STOR fileSize)
    private ArrayList<String> metaData;
    ////////////////////

    /////////////////// client side
    private CountDownLatch latch;
    ///////////////////


    public String getCommandId() {
        return commandId;
    }
    public CountDownLatch getLatch() {
        return latch;
    }
    public void setCommandId(String commandId) {  this.commandId = commandId; }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public Command(String header) {
        params = new ArrayList<>();
        this.header = header;
        commandId = UUID.randomUUID().toString();
    }

    public Command(String command, ArrayList<String> params) {
        this(command);
        this.params = params;
    }

    public void addParam(String param) {
        this.params.add(param);
    }

    public String getParam(int idx) {
        if(idx < 0 || idx >= params.size()){
            return null;}

        return this.params.get(idx);
    }
    public int getParamsCount(){return this.params.size();}
    public String getHeader(){return this.header;}


    @Override
    public boolean equals(Object other) {
        Command other_cmd = (Command)other;
        if(other_cmd.getHeader().equals(this.header) && other_cmd.getParamsCount() == this.getParamsCount()) {
            int n = other_cmd.getParamsCount();
            for(int i = 0; i<n; i++) {
                if(!this.params.get(i).equals(other_cmd.params.get(i))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public List<String> getParams() {
        return this.params;
    }

    // Meta data
    public List<String> getAllMetaData(){return this.metaData;}
    public void addMetaData(String data) {
        if(this.metaData == null) this.metaData = new ArrayList<>();
        this.metaData.add(data);
    }
    public void addMetaData(List<String> data) {
        if(data == null) return;
        for(String str : data) {
            addMetaData(str);
        }
    }
    public String getMetaData(int idx) {
        if(this.metaData == null) return null;
        if(idx >= this.metaData.size() || idx < 0) return null;
        return this.metaData.get(idx);
    }

    public Status prepare() {

        try {
            switch(CommandType.typeOf(this)) {

                case STOR -> {
                    String path = this.getParam(0);
                    File file = new File(path);
                    if(!file.exists()) {
                        LoggerManager.getInstance().warn("File not found.");
                        return Status.FAILED;
                    } else if(!file.isFile()) {
                        LoggerManager.getInstance().warn("Specified path doesn't represent a file.");
                        return Status.FAILED;
                    }

                    // add meta data
                    String absPath = file.getAbsolutePath();
                    long fileSize = file.length();
                    this.addMetaData(Long.toString(fileSize));
                    this.addMetaData(absPath);
                }

            }


        } catch (NoCommandWithSpecifiedHeaderException e) {
            LoggerManager.getInstance().error(String.format("'%s' isn't a valid command.", header));
            return Status.FAILED;
        }

        return Status.SUCCESS;
    }


    @Override
    public String toString() {
        String cmd = header;
        for(String param : params) {
            cmd = cmd.concat(" ".concat(param));
        }
        return cmd;
    }
}
