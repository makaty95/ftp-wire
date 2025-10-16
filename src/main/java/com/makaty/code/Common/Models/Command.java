package com.makaty.code.Common.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Command {
    private String header;
    private ArrayList<String> params;

    private String commandId;
    private CountDownLatch latch;

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
        commandId = UUID.randomUUID().toString();
    }

    public void addParam(String param) {
        this.params.add(param);
    }

    public String getParam(int idx) {
        if(idx < 0 || idx >= params.size()){
            System.out.println("[DEBUG] Invalid param access: idx=" + idx + ", size=" + params.size());
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
}
