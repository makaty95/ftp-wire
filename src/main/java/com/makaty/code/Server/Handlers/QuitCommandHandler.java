package com.makaty.code.Server.Handlers;


import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.*;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Tasks.SendPacketTask;

public class QuitCommandHandler implements CommandHandler {

    @Override
    public Void handle(Command command, Session clientSession) {
        /*
             * 1) validate if the command signature is valid
             *      1.1) check validation
             * 2) try to disconnect the user (with some functionality)
        */
        if(!CommandType.QUIT.isValidSignature(command)) {

            /// 1) log some logs with the logger
            /// 2) schedule a proper message to the client
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession, command.getCommandId())
            );
            return null;
        }

        if(clientSession != null) {
            // send quit affirm packet: this packet will contain only the session ID of the client.
            ReplyPacket replyPacket = ReplyType.QUIT_AFFIRM.createPacket(command.getCommandId(), clientSession.getSessionId());

            // submitting task to the dispatcher
            TaskDispatcher.getInstance().submitSyncTask(new SendPacketTask(clientSession.getClientProfile(), replyPacket));
            Server.serverLogger.info(String.format("[Client \"%s\" Disconnected]", clientSession.getClientName()));
            Server.sessionRegistry.terminateSession(clientSession.getSessionId());
        }


        return null;
    }
}
