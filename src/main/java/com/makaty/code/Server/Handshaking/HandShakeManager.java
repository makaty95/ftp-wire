package com.makaty.code.Server.Handshaking;

import com.makaty.code.Common.Packets.Packet;
import com.makaty.code.Common.Packets.HansShaking.DonePacket;
import com.makaty.code.Common.Packets.HansShaking.HelloPacket;
import com.makaty.code.Common.Packets.HansShaking.PairPacket;
import com.makaty.code.Common.Packets.HansShaking.WelcomePacket;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.ServerConfig;
import com.makaty.code.Server.Tasks.SendPacketTask;
import com.makaty.code.Server.Exceptions.CannotReadPacketException;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Common.Models.Status;
import com.makaty.code.Server.Models.TaskDispatcher;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class HandShakeManager {


    public Void performHandShake(SocketChannel commandSocketChannel) throws CannotReadPacketException {

        // read the hello packet which is sent from the client
        Packet hello;
        try{hello = PacketType.HELLO.getReader().read(commandSocketChannel);} catch (IOException e) {
            Server.serverLogger.error("Could not read handshaking hello packet!, handshaking terminated.");
            throw new CannotReadPacketException("Could not read hello packet");
        }

        // generate id for the new session
        String sessionId = UUID.randomUUID().toString();

        // generating a session instance
        Session session = new Session(sessionId, ((HelloPacket)hello).getUsername(), commandSocketChannel);

        // storing session in the registry
        Server.sessionRegistry.add(session);

        // authentication is true (for now) (later u can check password.)
        Server.sessionRegistry.get(sessionId).setAuthenticated(true);

        // sending back a welcome message to the client
        WelcomePacket packet = new WelcomePacket(sessionId, ServerConfig.DATA_HOST, ServerConfig.DATA_PORT);
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(session.getClientProfile(), packet));

        // report that client connected
        Server.serverLogger.reportClientConnection(session);
        return null;
    }

    public Void completeHandShake(SocketChannel dataSocketChannel) throws CannotReadPacketException {

        // read the pair packet which is sent from the client
        Packet packet;
        try{packet = PacketType.PAIR_REQUEST.getReader().read(dataSocketChannel);} catch (IOException e) {
            Server.serverLogger.error("Could not read handshaking pair packet!, handshaking terminated.");
            throw new CannotReadPacketException("Could not read pair packet");
        }


        // check if the session id exists in the session registry
        String sessionId = ((PairPacket)packet).getSessionId();
        Session session = Server.sessionRegistry.get(sessionId);

        if(session == null) {
            Server.serverLogger.warn("No session exist for the client");
            return null;
        }

        boolean success = false;
        if(session.isAuthenticated()
                && !session.isClosed()
                && session.getStatus() == Status.PENDING
        ) {

            // complete the registration
            session.completeRegistration(dataSocketChannel);
            success = true;
        }

        // sending the done packet with success value.
        DonePacket donePacket = new DonePacket(success);
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(session.getClientProfile(), donePacket));

        // register the command channel to make the client able to send commands
        Server.selectorDispatcher.registerCommandChannel(session.getCommandSocketChannel(), sessionId);

        Server.serverLogger.info(String.format("HandShaking done with Client \"%s\"", session.getClientName()));
        return null;

    }
}
