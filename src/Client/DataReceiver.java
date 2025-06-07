package Client;

import Common.Reply;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataReceiver {
    private         boolean     receiveDone;
    private         double      currentPercentage;
    private final   String      fileName;
    private final   long        fileSize;
    private final   char[]      loadingBar;
    private final   char        loadingChar;
    private final   int         barSize;

    public DataReceiver(Reply reply) {
        System.out.println("content: " + reply.getContent());
        String[] splits = reply.getContent().trim().split(" ");

        this.fileName = splits[0];
        this.fileSize = Long.valueOf(splits[1]);

        System.out.println("fileName: " + fileName);
        System.out.println("fileSize: " + fileSize);

        barSize = 50;
        receiveDone = false;
        loadingBar = new char[barSize];
        loadingChar = '#';
        for(int i = 0; i<barSize; i++) {
            loadingBar[i] = ' ';
        }
    }

    public void displayLoadingBar() {
        String loadingString = new String(loadingBar);
        System.out.printf("\r[%s] %.2f%% loaded.", loadingString, currentPercentage);
    }

    public void updateLoadingBar() {
        int threshold = (int)currentPercentage * barSize / 100;
        for(int i = 0; i<threshold; i++) {
            loadingBar[i] = loadingChar;
        }
    }

    public void updatePercentage(long loaded) {
        currentPercentage = ((double)loaded/fileSize)*100;
    }

    public void updateState(long loaded) {
        updatePercentage(loaded);
        updateLoadingBar();
        displayLoadingBar();

        // if the all file bytes are received terminate the process.
        if(loaded >= fileSize) receiveDone = true;
    }

    public void receive(InputStream inputStream) {



        // receive the data via the connection stream;
        try(FileOutputStream fileOutputStream = new FileOutputStream(this.fileName)) {

            System.out.println("\rReceiving: " + fileName + "...");
            byte[] buffer = new byte[4096];

            int numOfBytes;
            long loaded = 0;
            while(!receiveDone && (numOfBytes = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, numOfBytes);
                loaded += numOfBytes;
                updateState(loaded);

            }
            System.out.println("File received.");
        } catch(IOException ex) {
            System.out.println("The server may have disconnected, failed to receive the file.");
        }

    }
}
