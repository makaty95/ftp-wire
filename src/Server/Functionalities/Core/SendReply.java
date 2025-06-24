package Server.Functionalities.Core;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import Common.Reply;
import Common.Status;
import Common.Utility;
import Server.Task;

public class SendReply extends Task {
    
    Reply reply;
    SocketChannel commandSocketChannel;
    public SendReply(Reply reply, SocketChannel commandSocketChannel) {
        this.reply = reply;
        this.commandSocketChannel = commandSocketChannel;
    }
    

    
    @Override
    public Status call() {
        String message = Utility.toMessage(reply);
        try {

            // wrapping the bytes into a ByteBuffer
            // we can not use OutputStream here because this is a blocking IO 
            // and we are working with NonBlocking IO here
            ByteBuffer data = ByteBuffer.wrap(message.getBytes());

            // writing data to the client
            commandSocketChannel.write(data); 

            return Status.REPLY_SENT;
            
        } catch(Exception e) {
            System.err.println("Unhandled Exception in SendReply - 2893");
            System.err.println(e);
            return Status.CONNECTION_LOST;    
        }



    }


}