package Server;

import Common.Command;
import Common.Reply;
import Common.Status;
import Common.Utility;
import Server.Functionalities.Core.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class ServerService extends Thread {

    private ThreadPool threadPool;
    public Selector commandSelector;
    private Map<SocketChannel, SocketChannel> hashStore;

    public ServerService() throws IOException {
        hashStore = new HashMap<>();
        this.commandSelector = Selector.open();
        threadPool = new ThreadPool(5);
    }

    // to have the ability to get connection socket channel
    public void addToHashStore(SocketChannel commandSockerChannel, SocketChannel connectionSockerChannel) {
        this.hashStore.put(commandSockerChannel, connectionSockerChannel);
    }

    ////////// Functions///////////
    public Status sendFile(String filePath, String fileName, long fileSize, SocketChannel commandSocketChannel, SocketChannel connectionSocketChannel) {

        Task task = new SendFile(filePath, fileName, fileSize, commandSocketChannel, connectionSocketChannel, this);
        return this.threadPool.ExecuteSch(task);

    }



    public Status sendReply(Reply reply, SocketChannel commandSocketChannel) throws IOException {
        Task task = new SendReply(reply, commandSocketChannel);
        return threadPool.ExecuteNow(task);
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
            case Status.CONNECTION_TERMINATED:
                System.out.println("Connection terminated.");
                break;
            default:
                System.out.println("Mysterious operation status.");
                break;
        }
    }

    public Status sendStatusMessage(Status state, SocketChannel commandSocketChannel) throws IOException {

        /*
         * - this is just a start, more details on the errors, status will and should be
         * added after this.
         */

        String message, code;
        switch (state) {
            case Status.CONNECTION_ESTABLISHED:
                message = "Connection established with the server. type cmds to see all commands.";
                code = "100";
                break;

            case Status.FILE_SENT:
                message = "File received successfully.";
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
        return sendReply(reply, commandSocketChannel);

    }

    public Status sendCMDS(SocketChannel commandSocketChannel) {
        Task task = new SendCMDS(commandSocketChannel, this);
        return this.threadPool.ExecuteSch(task);
    }

    ///////////////////////
    public Status executeCommand(Command command, SocketChannel commandSocketChannel, SocketChannel connectionSocketChannel)
            throws IOException {

        Status executionStatus;

        // check the command.
        executionStatus = Utility.isValidCommand(command);

        if (executionStatus == Status.STABLE) {
            // execute it
            String header = command.getHeader();
            if (header.equals("get_file")) {
                String workingDir = System.getProperty("user.dir");
                System.out.println("The current WorkDir = " + workingDir);


                // BUG/ Invalid path is proceeding.
                String filePath = command.getParam(0);
                File file = new File(filePath);
                executionStatus = file.exists() ? executionStatus : Status.INVALID_PATH;

                if (executionStatus == Status.STABLE) {
                    String fileName = file.getName();
                    long size = file.length();

                    if (size == Long.MAX_VALUE) {
                        System.out.println("warning, file size reached long max!");
                    }

                    if (command.getParamsCount() == 1) { // send the file with the same name
                        executionStatus = sendFile(filePath, fileName, size, commandSocketChannel, connectionSocketChannel);
                    } else { // send the file with a new name
                        executionStatus = sendFile(filePath, command.getParam(1), size, commandSocketChannel, connectionSocketChannel);
                    }

                }

                /*
                 * client:
                 * 1. commandSocketChannel
                 * 2. connectionSocketChannel
                 */

            } else if (header.equals("bye")) {
                executionStatus = Status.CLIENT_QUIT;
            } else if (header.equals("cmds")) {
                executionStatus = sendCMDS(commandSocketChannel);
            }
        }

        // show console message
        showMessage(executionStatus);

        // send the status for that command
        if (!(Status.isDisconnection(executionStatus))) {
            sendStatusMessage(executionStatus, commandSocketChannel);
            showMessage(Status.REPLY_SENT);
        }

        return executionStatus;

    }

    private SocketChannel get_CON_SocketChannel(SocketChannel commandSocketChannel) {
        SocketChannel connectionSocketChannel = this.hashStore.get(commandSocketChannel);
        return connectionSocketChannel;

    }

    public Command recieveCommand(SocketChannel commandSocketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int bytes = commandSocketChannel.read(buffer);

        if(bytes <= 0) return null;

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return Utility.toCommand(data);
        
    }

    public void terminateConnection(SocketChannel comSocketChannel, SocketChannel conSocketChannel) {
        try {
            // close all sockets
            conSocketChannel.close();
            comSocketChannel.close();

            // remove thee link from the hashMap
            this.hashStore.remove(comSocketChannel);

        } catch(IOException e) {
            showMessage(Status.SERIOUS_ERROR);
        }

        showMessage(Status.CONNECTION_TERMINATED);

    }
    

    @Override
    public void run() {
        while (true) {
            try {
                //System.out.println("somthing... :()");
                commandSelector.select(); // Blocks until any channel is ready
            } catch(Exception e) {
                System.err.println("Unhandled Exception in ServerService.java - 1920");
                System.err.println(e);
                return;
            }

            //System.out.println("Flag0 Fired.");
            

            for (SelectionKey key : commandSelector.selectedKeys()) {
                //System.out.println("Flag1 Fired.");
                if (key.isReadable()) {

    
                    try {
                        // getting the commandSocketChannel
                        SocketChannel commandSocketChannel = (SocketChannel) key.channel();
    
                        //System.out.println("Flag2 Fired.");
                        
                        // extracting the command from the InputStream.
                        Command command = recieveCommand(commandSocketChannel);
    

                        //System.out.println("Flag3 Fired.");
                        SocketChannel connectionSocketChannel = get_CON_SocketChannel(commandSocketChannel);
    
                        //System.out.println("Flag4 Fired.");
                        // execute the user command and send the status message
                        Status state = executeCommand(command, commandSocketChannel, connectionSocketChannel);
    
                        //System.out.println("Flag5 Fired.");
                        if (Status.isDisconnection(state)) {
                            terminateConnection(commandSocketChannel, connectionSocketChannel);
                        }

                    } catch(Exception e) {
                        System.err.println("Unhandled Exception in ServerService.java - 4839");
                        System.err.println(e);
                        return;
                    }

                }
            }

            commandSelector.selectedKeys().clear();

        }

    }


}
