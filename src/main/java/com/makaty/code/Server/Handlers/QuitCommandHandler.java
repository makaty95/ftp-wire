package com.makaty.code.Server.Handlers;


import com.makaty.code.Common.Models.Command;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Models.*;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;

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
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND, clientSession)
            );
            return null;
        }

        if(clientSession != null) {
            Server.serverLogger.info(String.format("[Client \"%s\" Disconnected]", clientSession.getClientName()));
            Server.sessionRegistry.terminateSession(clientSession.getSessionId());
        }


        return null;
    }
}
