package Client;

import Common.Reply;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataReceiver {
    String fileName;
    long fileSize;

    public DataReceiver(Reply reply) {
        String[] splits = reply.getContent().trim().split(" ");
        this.fileName = splits[0];
        this.fileSize = Long.valueOf(splits[1]);

    }

    public void updateBar(long loaded) {
        System.out.println("\r" + ((float)loaded/fileSize)*100.f + "% Loaded.");
    }

    public void receive(InputStream inputStream) {



        // receive the data via the connection stream;
        try(FileOutputStream fileOutputStream = new FileOutputStream(this.fileName)) {

            System.out.println("\rReceiving: " + fileName + "...");
            byte[] buffer = new byte[4096];

            int numOfBytes;
            long loaded = 0;
            while((numOfBytes = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, numOfBytes);
                loaded += numOfBytes;
                updateBar(loaded);
            }
            inputStream.close();
            System.out.println("File received.");
        } catch(IOException ex) {
            System.out.println("The server may have disconnected, failed to receive the file.");
        }

    }
}
