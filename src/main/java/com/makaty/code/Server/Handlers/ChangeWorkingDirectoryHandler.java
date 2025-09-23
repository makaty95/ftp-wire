package com.makaty.code.Server.Handlers;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Models.Status;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Models.TaskDispatcher;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Tasks.SendPacketTask;

import java.io.IOException;

public class ChangeWorkingDirectoryHandler implements CommandHandler {
    @Override
    public Void handle(Command command, Session clientSession) throws IOException {

        // 0) validate it is in a valid shape
        if(!CommandType.CWD.isValidSignature(command)) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession)
            );
            return null;
        }

        // 1) change working dir
        String directory_name = command.getParam(0);
        Status status = clientSession.getClientProfile().changeWorkingDir(directory_name);
        if(status == Status.FILE_NOT_DIR) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.FILE_NOT_DIR, clientSession)
            );
            return null;
        }

        if(status == Status.NO_FILE_EXISTS) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.WRONG_FILE_NAME, clientSession)
            );
            return null;
        }


        // 2) send new relative path to the client
        ReplyPacket replyPacket = ReplyType.CWD_INFO.createPacket(
                clientSession.getClientProfile().getRelativeWorkingDir(),
                clientSession.getClientProfile().getAbsoluteWorkingDir()
        );

        // submitting task to the dispatcher
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(clientSession.getClientProfile(), replyPacket));
        return null;
    }
}
