package Common;

import java.util.ArrayList;

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
        return other_cmd.getHeader().equals(this.header) && other_cmd.getParamsCount() == this.getParamsCount();
    }


}
