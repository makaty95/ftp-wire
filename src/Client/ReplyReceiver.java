package Client;

import Common.Reply;
import Common.Utility;

import java.io.IOException;
import java.util.ArrayList;

public class ReplyReceiver extends Thread {
    protected ClientUI ui;

    public ReplyReceiver(ClientUI ui) {
        this.ui = ui;
    }

    public ArrayList<Reply> getReplies() throws IOException {
        byte[] buffer = new byte[4096];
        int bytes = ui.commandIn.read(buffer);

        ArrayList<Reply> replies = new ArrayList<>();
        String text = new String(buffer).trim();
        String splits[] = text.split("#");

        for(int i = 0; i<splits.length-1; i+=2) {
            if(splits[i].isEmpty() || splits[i].isBlank()) {
                i--;
                continue;
            }
            replies.add(new Reply(splits[i], splits[i+1]));
        }

        return replies;


    }

    public void displayReply(Reply reply) {
        System.out.print("\r<Server>: " + reply.getContent() + "\nCMD> ");
    }

    public void dispatchReply(Reply reply) {
        if(Utility.isConnectionReply(reply)) {
            new DataReceiver(reply).receive(ui.connectionIn);;

        } else {
            displayReply(reply);
        }
    }


    @Override
    public void run() {

        try{
            while(ui.isConnected) {
                if(ui.commandIn.available() > 0) {
                    // read command
                    ArrayList<Reply> replies = getReplies();
                    for(Reply reply : replies) {
                        dispatchReply(reply);
                    }

                }

            }

        } catch (Exception e) {
            System.out.println("Server disconnected");
        }


    }

}
