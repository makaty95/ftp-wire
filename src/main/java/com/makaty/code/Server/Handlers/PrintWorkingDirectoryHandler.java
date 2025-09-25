package com.makaty.code.Server.Handlers;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Models.TaskDispatcher;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Tasks.SendPacketTask;

import java.io.IOException;

public class PrintWorkingDirectoryHandler implements CommandHandler {

    @Override
    public Void handle(Command command, Session clientSession) throws IOException {

        // 1) validate it is in a valid shape
        if(!CommandType.PWD.isValidSignature(command)) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession)
            );
            return null;
        }

        // creating response packet which have the full path of the current working directory
        ReplyPacket replyPacket = ReplyType.PWD_INFO.createPacket(
                clientSession.getClientProfile().getRelativeWorkingDir(),
                clientSession.getClientProfile().getAbsoluteWorkingDir()
        );

        // submitting task to the dispatcher
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(clientSession.getClientProfile(), replyPacket));
        return null;
    }
}
