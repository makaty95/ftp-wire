package com.makaty.code.Server.Handlers;


import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Tasks.SendPacketTask;
import com.makaty.code.Server.Models.*;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;

import java.io.IOException;

public class HelpCommandHandler implements CommandHandler {

    @Override
    public Void handle(Command command, Session clientSession) throws IOException {
         /*
         * 1) validate if the command signature is valid
         *      1.1) check validation
         * 2) initialize a task of type SendMessageTask which will contain the commands
         * 3) dispatch the task via calling dispatchTask method
         * 4) return the status of the method call
         */



        // 1) validate it is in a valid shape
        if(!CommandType.HELP.isValidSignature(command)) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession,command.getCommandId())
            );
            return null;
        }

        // creating response packet which have the descriptions to the commands
        ReplyPacket replyPacket = ReplyType.MESSAGE.createPacket(command.getCommandId(), CommandType.COMMANDS_INFO);

        // submitting task to the dispatcher
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(clientSession.getClientProfile(), replyPacket));
        return null;
    }

}
