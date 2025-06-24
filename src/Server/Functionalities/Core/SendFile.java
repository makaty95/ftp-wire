package Server.Functionalities.Core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import Common.Reply;
import Common.Status;
import Server.ServerService;
import Server.Task;

public class SendFile extends Task {
    

    private SocketChannel commandSocketChannel, connectionSocketChannel;
    ServerService currentService;
    String filePath, fileName;
    long fileSize;
    public SendFile(String filePath, String fileName, long fileSize, SocketChannel commandSocketChannel, SocketChannel connectionSocketChannel, ServerService currentService) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.commandSocketChannel = commandSocketChannel;
        this.connectionSocketChannel = connectionSocketChannel;
        this.currentService = currentService;
    }
    

    
    @Override
    public Status call() {

        
        //send a status message with the file name and size to be received;
        String content = fileName + " " + fileSize;
        try {
            currentService.sendReply(new Reply("103", content), commandSocketChannel);
        } catch(Exception e) {
            System.err.println("Unhandled Exception in SendFile.java - 1192 - [Status.CONNECTION_LOST returned]");
            return Status.CONNECTION_LOST;
        }

      
        long BS = fileSize;
        long KS = BS/1024;
        long MS = KS/1024;
        long GS = MS/1024;

        System.out.println("File size: " + BS + "B = "+ KS + "KB = " + MS + "MB = " + GS + "GB");
        System.out.println("Sending file...");

        
        

        // send the file to the client.
        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] inputBuffer = new byte[4096];
            int numOfBytes;

            // copying file contents to the client
            while((numOfBytes = fileInputStream.read(inputBuffer)) != -1) {

                // wrapping data
                ByteBuffer data = ByteBuffer.wrap(inputBuffer, 0, numOfBytes);
                while(data.hasRemaining()) {
                    // send the bytes to the client
                    connectionSocketChannel.write(data);
                }

            }

        }catch(FileNotFoundException e) {
            return Status.INVALID_PATH;
        } catch(IOException e) {
            return Status.CONNECTION_LOST;
        } 

        return Status.FILE_SENT;




        


    }


}
