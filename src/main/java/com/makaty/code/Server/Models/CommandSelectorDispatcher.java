package com.makaty.code.Server.Models;

import com.makaty.code.Server.Handlers.CommandErrorHandler;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Handlers.CommandHandler;
import com.makaty.code.Common.Packets.Communication.CommandPacket;
import com.makaty.code.Common.Exceptions.NoCommandWithSpecifiedHeaderException;
import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class CommandSelectorDispatcher extends Thread {

    private final Selector selector;
    private boolean working;

    public void terminate() {
        working = false;
        selector.wakeup();
    }

    public CommandSelectorDispatcher() {
        try {
            this.selector = Selector.open();
            working = true;
        } catch (IOException e) {
            //TODO: add logs here
            throw new RuntimeException("Failed to open selector", e);
        }

    }

    public void registerCommandChannel(SocketChannel commandSocketchannel, String sessionId) {
        try {
            commandSocketchannel.configureBlocking(false);
            commandSocketchannel.register(selector, SelectionKey.OP_READ, sessionId);
            Server.serverLogger.info("Registered channel for " + commandSocketchannel.getRemoteAddress());
            selector.wakeup();
        } catch (IOException e) {
            Server.serverLogger.error("Failed to register commandSocketChannel");
        }
    }

    public boolean isWorking() {
        return this.working;
    }

    @Override
    public void run() {
        Server.serverLogger.info("Command Selector Dispatcher is up and running.");

        while (working) {
            try {
                selector.select(); // Wait until something is ready

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;
                    if (key.isReadable()) {

                        try {
                            handleCommand((SocketChannel) key.channel());
                        } catch (RemoteDisconnectionException e) {
                            key.cancel();

                            //TODO: don't forget to check if the client really is quited when he disconnects.
                            String sessionId = (String) key.attachment();
                            CommandType.QUIT.getCommandHandler().handle(new Command(CommandType.QUIT.getHeader()), Server.sessionRegistry.get(sessionId));

                        }
                    }
                }

            } catch (IOException e) {
                Server.serverLogger.error("Selector error.");
            }
        }

        // close the selector
        try {
            selector.close();
        } catch (IOException e) {
            //TODO: add logs here
            throw new RuntimeException(e);
        }

    }

    private void handleCommand(SocketChannel commandSocketChannel) throws IOException {

        try {

            // reading the command packet
            CommandPacket commandPacket = (CommandPacket) PacketType.COMMAND.getReader().read(commandSocketChannel);


            Server.serverLogger.info("Command received.");
            TaskDispatcher.getInstance().submitSyncTask(() -> {
                try {

                    // get the client session
                    Session session = Server.sessionRegistry.get(commandPacket.getSessionId());

                    // if there is no session for that client (ignore it)
                    if(session == null) {
                        Server.serverLogger.warn(String.format("No session exist with sessionId \"%s\"", commandPacket.getSessionId()));
                        return null;
                    }
                    CommandType commandType = null;


                    try {
                        // determining the type of the command
                        commandType = CommandType.typeOf(commandPacket.getCommand());
                    } catch (NoCommandWithSpecifiedHeaderException e) {
                        //guard against null values
                        String commandId= commandPacket.getCommand().getCommandId()!=null?commandPacket.getCommand().getCommandId() :"Unknown cmd";
                        TaskDispatcher.getInstance().submitAsyncTask(() ->
                                new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND, session, commandId)
                        );
                        return null;
                    }

                    // determining the command handler
                    CommandHandler handler = commandType.getCommandHandler();

                    // handling the command
                    handler.handle(commandPacket.getCommand(), session);


                } catch (IOException e) {
                    Server.serverLogger.error("IO Exception while handling a command.");
                }

                return null;
            });

        } catch (RemoteDisconnectionException e) {
            Server.serverLogger.warn("Client may have disconnected.");
            throw e;
        }

    }


}
