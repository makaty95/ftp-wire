package com.makaty.code.Server.Handlers;


import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Tasks.SendFileTask;
import com.makaty.code.Server.Tasks.SendPacketTask;
import com.makaty.code.Server.Models.*;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;

import java.io.File;


public class RetrieveFileCommandHandler implements CommandHandler {



    @Override
    public Void handle(Command command, Session clientSession) {
        /*
        * 1) validate if the command signature is valid
        *   1.1) check validation
        * 2) extract the path of the wanted file from command
        * 3) validate if the file in that path exists or not
        *   3.1) check validation
        * 4) if all validations are ok then initialize a task of type SendFileTask
        *     4.1) dispatch the task via calling the task dispatcher
        *     4.2) return the status which is returned by the dispatcher function
        * 5) if some validation failed
        *     5.1) return the status message which represents the validation
        *
        */
        if(!CommandType.RETR.isValidSignature(command)) {

            /// 1) log some logs with the logger
            /// 2) schedule a proper message to the client
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession)
            );
            return null;
        }

        String filePath = command.getParam(0);
        File file = new File(filePath);
        String newFileName = command.getParamsCount() == 2 ? command.getParam(1) : file.getName();

        if(!file.exists()) {

            /// 1) log some logs with the logger
            /// 2) schedule a proper message to the client
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_PATH, clientSession)
            );
            return null;
        }

        // Send FileInfo packet to the client first (Synchronously)
        ReplyPacket replyPacket = ReplyType.FILE_INFO.createPacket(newFileName, file.length());
        SendPacketTask packetTask = new SendPacketTask(clientSession.getClientProfile(), replyPacket);
        TaskDispatcher.getInstance().submitSyncTask(packetTask); //TODO: (optimization) could we convert this to asynchronous task ?


        // Send file to the client (Asynchronously)
        SendFileTask task = new SendFileTask(clientSession.getClientProfile(), file, 0);
        TaskDispatcher.getInstance().submitAsyncTask(task);

        return null;
    }
}
