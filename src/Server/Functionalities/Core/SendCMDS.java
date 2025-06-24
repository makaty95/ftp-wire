package Server.Functionalities.Core;


import java.nio.channels.SocketChannel;
import java.util.Map;
import Common.Status;
import Common.CommandInfo;
import Common.Reply;
import Common.Utility;
import Server.ServerService;
import Server.Task;

public class SendCMDS extends Task{

    SocketChannel commandSocketChannel;
    ServerService currentServerService;
    public SendCMDS(SocketChannel commandSocketChannel, ServerService currentServerService) {
        this.commandSocketChannel = commandSocketChannel;
        this.currentServerService = currentServerService;
    }

    @Override
    public Status call() {
        int cnt = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-20s | %-30s | %-40s |\n","Command", "Parameters", "Description"));
        sb.append("----------------------------------------------------------------------------------------------------\n");
        for(Map.Entry<String, CommandInfo> entry : Utility.commands.entrySet()) {


            StringBuilder params = new StringBuilder();
            for(String mParam : entry.getValue().mParams) {
                params.append(mParam);
                params.append(" ");
            }

            for(String oParam : entry.getValue().oParams) {
                params.append(oParam);
                params.append(" ");
            }

            sb.append(String.format("| %-20s | %-30s | %-40s |\n", "[" + cnt + "]" + entry.getKey(), params.toString(), entry.getValue().description));
            //sb.append(String.format("| %-40s", params.toString()));
            //sb.append(String.format("| %10s",entry.getValue().description));

            cnt++;
        }
        sb.append("----------------------------------------------------------------------------------------------------\n");

        Reply reply = new Reply("102", sb.toString());
        try {
            currentServerService.sendReply(reply, commandSocketChannel);
        } catch(Exception e) {
            System.err.println("Unhandled Exception in SendCMDS.java - 2902 - [Status.CONNECTION_LOST returned]");
            return Status.CONNECTION_LOST;
        }
        return Status.STABLE;

    }
    
}
