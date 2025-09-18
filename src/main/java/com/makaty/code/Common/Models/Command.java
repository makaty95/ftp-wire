package com.makaty.code.Common.Models;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String header;
    private ArrayList<String> params;

    public Command(String header) {
        params = new ArrayList<>();
        this.header = header;
    }

    public Command(String command, ArrayList<String> params) {
        this(command);
        this.params = params;
    }

    public void addParam(String param) {
        this.params.add(param);
    }

    public String getParam(int idx) {
        if(idx < 0 || idx >= params.size()) return null;

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
