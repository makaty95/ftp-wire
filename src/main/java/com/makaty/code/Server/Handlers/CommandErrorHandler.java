package com.makaty.code.Server.Handlers;

import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Common.Types.PacketType;
import com.makaty.code.Server.Models.TaskDispatcher;


public class CommandErrorHandler implements ErrorHandler {

    /**
     * Handles command-related errors in a centralized way.
     *
     * @param errorType The type of error to handle.
     * @param session   The session of the client to send the response to.
     */
    @Override
    public Void handle(ErrorType errorType, Session session) {
        // Log the error internally
        Server.serverLogger.warn(errorType.getLogMessage());

        // Create a proper message packet for the client
        ReplyPacket replyPacket = ReplyType.MESSAGE.createPacket(errorType.getClientMessage());

        // Send the packet asynchronously to the client
        TaskDispatcher.getInstance().submitAsyncTask(() ->
                PacketType.REPLY.getWriter().write(replyPacket, session.getCommandSocketChannel())
        );

        return null;
    }
}
