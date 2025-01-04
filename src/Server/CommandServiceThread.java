package Server;

import Common.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;


public class CommandServiceThread  {

    ServerServiceThread serverThread;
    Command command;

    public CommandServiceThread(ServerServiceThread thread, Command command) {
        this.serverThread = thread;
        this.command = command;
    }


    // Commands functions....
    public Status sendFile(String filePath, String fileName, long fileSize) throws IOException {


        //send a status message with the file name and size to be received;
        String content = fileName + " " + fileSize;
        serverThread.sendReply(new Reply("103", content));

        long BS = fileSize;
        long KS = BS/1024;
        long MS = KS/1024;
        long GS = MS/1024;

        System.out.println("File size: " + BS + "B = "+ KS + "KB = " + MS + "MB = " + GS + "GB");
        System.out.println("Sending file..");

        // send the file to the client.
        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            byte[] inputBuffer = new byte[4096];
            int numOfBytes;
            // copying file contents to the client
            while((numOfBytes = fileInputStream.read(inputBuffer)) != -1) {
                // send the bytes to the client
                serverThread.connectionOutStream.write(inputBuffer, 0, numOfBytes);
                serverThread.connectionOutStream.flush();
            }
            serverThread.connectionOutStream.flush();
            serverThread.connectionOutStream.close();

        }catch(FileNotFoundException e) {
            return Status.INVALID_PATH;
        } catch(IOException e) {
            return Status.CONNECTION_LOST;
        }

        return Status.FILE_SENT;

    }

    public Status getCMDS() throws IOException {
        int cnt = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("\n-------------------------------------\n");
        for(Map.Entry<String, CommandInfo> entry : Utility.commands.entrySet()) {
            sb.append("[");
            sb.append(cnt);
            sb.append("]");
            sb.append(entry.getKey());
            sb.append("\t");

            for(String mParam : entry.getValue().mParams) {
                sb.append(mParam);
                sb.append(" ");
            }

            for(String oParam : entry.getValue().oParams) {
                sb.append(oParam);
                sb.append(" ");
            }

            sb.append(entry.getValue().description);
            sb.append("\n");
            cnt++;
        }
        sb.append("-------------------------------------");

        Reply reply = new Reply("102", sb.toString());
        serverThread.sendReply(reply);
        return Status.STABLE;

    }


    // the main execute command
    public Status execute() throws IOException {
        Status executionStatus;

        // check the command.
        executionStatus = Utility.isValidCommand(command);


        if(executionStatus == Status.STABLE) {
            // execute it
            String header = command.getHeader();
            if(header.equals("get_file")) {

                //BUG/ Invalid path is proceeding.
                String filePath = command.getParam(0);
                File file = new File(filePath);
                executionStatus = file.exists() ? executionStatus : Status.INVALID_PATH;


                if(executionStatus == Status.STABLE) {
                    String fileName = file.getName();
                    long size = file.length();

                    if(size == Long.MAX_VALUE) {
                        System.out.println("warning, file size reached long max!");
                    }

                    if(command.getParamsCount() == 1) { // send the file with the same name
                        executionStatus = sendFile(filePath, fileName, size);
                    } else { // send the file with a new name
                        executionStatus = sendFile(filePath, command.getParam(1), size);
                    }

                }


            } else if(header.equals("bye")) {
                serverThread.isConnected = false;
                executionStatus = Status.CLIENT_QUIT;
            } else if(header.equals("cmds")) {
                executionStatus = getCMDS();
            }
        }

        return executionStatus;

    }

}
