package Server;

import Common.Command;
import Common.Reply;
import Common.Status;
import Common.Utility;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerServiceThread extends Thread {


    boolean isConnected;

    // sockets
    Socket commandSocket;
    Socket connectionSocket;

    // streams
    InputStream connectionInStream, commandInStream;
    OutputStream connectionOutStream, commandOutStream;

    public ServerServiceThread(Socket commandSocket, Socket connectionSocket) {

        isConnected = true;

        // assigning sockets
        this.commandSocket = commandSocket;
        this.connectionSocket = connectionSocket;

        // assigning streams
        try {
            this.commandInStream = commandSocket.getInputStream();
            this.commandOutStream = commandSocket.getOutputStream();
            this.connectionInStream = connectionSocket.getInputStream();
            this.connectionOutStream = connectionSocket.getOutputStream();

        }catch(Exception e) {
            // showMessage(Status.CONNECTION_LOST);
        }

    }

    public Status sendReply(Reply reply) throws IOException  {
        String message = Utility.toMessage(reply);
        commandOutStream.write(message.getBytes()); //ex
        commandOutStream.flush();
        return Status.REPLY_SENT;
    }

    public Command recieveCommand() throws IOException  {

        byte[] buffer = new byte[4096];

        int bytesNum = commandInStream.read(buffer);
        return Utility.toCommand(buffer);
    }

    public void showMessage(Status status) {
        switch (status) {
            case Status.CONNECTION_LOST:
                System.out.println("Client disconnected.");
                break;
            case Status.SERIOUS_ERROR:
                System.out.println("Serious error, terminating...");
                break;
            case Status.INVALID_COMMAND:
                System.out.println("Client invalid command (command refused).");
                break;
            case Status.INVALID_PARAMS_COUNT:
                System.out.println("Invalid Command arguments (command refused).");
                break;
            case Status.INVALID_PATH:
                System.out.println("Invalid File path (command refused)");
                break;
            case Status.STABLE:
                break;
            case Status.REPLY_SENT:
                System.out.println("Reply sent.");
                break;
            case Status.FILE_SENT:
                System.out.println("File sent.");
                break;
            case Status.CONNECTION_ESTABLISHED:
                System.out.println("Client Connected.");
                break;
            case Status.CLIENT_QUIT:
                System.out.println("Client quit.");
                break;
            default:
                System.out.println("Mysterious operation status.");
                break;
        }
    }

    public void terminateConnection() {
        try {
            // terminate
            this.connectionInStream.close();
            this.connectionOutStream.close();
            this.connectionSocket.close();

            this.commandInStream.close();
            this.commandOutStream.close();
            this.commandSocket.close();

        } catch(IOException e) {
            showMessage(Status.SERIOUS_ERROR);
        }

    }

    public Status sendStatusMessage(Status state) throws IOException {

        /*
            - this is just a start, more details on the errors, status will and should be added after this.
         */

        String message,code;
        switch (state) {
            case Status.CONNECTION_ESTABLISHED:
                message = "Connection established with the server.";
                code = "100";
                break;

            case Status.FILE_SENT:
                message =  "File received successfully.";
                code = "101";
                break;

            case Status.INVALID_COMMAND:
                message = "Invalid command.";
                code = "201";
                break;

            case Status.INVALID_PARAMS_COUNT:
                message = "Invalid command parameters or count.";
                code = "202";
                break;

            case Status.INVALID_PATH:
                message = "Invalid file path provided.";
                code = "203";
                break;

            case Status.SERIOUS_ERROR:
                message = "Server panic! (please report that bug).";
                code = "300";
                break;

            default:
                return Status.STABLE;
        }

        Reply reply = new Reply(code, message);

        return sendReply(reply);

    }

    public void executeCommand(Command command) throws IOException {

        // first execute the command
        Status state = new CommandServiceThread(this, command).execute();

        // show console message
        showMessage(state);

        // send the status for that command
        if(!(Status.isDisconnection(state))) {
            sendStatusMessage(state);
            showMessage(Status.REPLY_SENT);
        }

    }

    @Override
    public void run() {
        /*
            | Steps:
            [1] send status message for the client connectivity
            [2] get a command from the client
            [3] execute that command
            [4] report the client for the command result

            | Note:
            - for now each client connection will be able to execute a single command and then
             terminates safely.
             - but the server will still accept multiple users at the same time.
         */

        try {
            // send first status message to the client.
            showMessage(sendStatusMessage(Status.CONNECTION_ESTABLISHED));

            while(isConnected) {
                if(commandInStream.available() > 0) { // if there is something to read.
                    // receive client command
                    Command command = recieveCommand();

                    // execute the user command and send the status message
                    executeCommand(command);
                }
            }

        } catch(IOException e) {
            showMessage(Status.CONNECTION_LOST);
        }

        System.out.println("terminating connection..");
        terminateConnection();
    }
}
