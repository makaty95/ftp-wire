package com.makaty.code.Server.Handlers;

import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Models.TaskDispatcher;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Server;
import com.makaty.code.Server.Tasks.ReceiveFileTask;
import com.makaty.code.Server.Tasks.SendPacketTask;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StoreFileCommandHandler implements CommandHandler {

    @Override
    public Void handle(Command command, Session clientSession) {

        if(!CommandType.STOR.isValidSignature(command)) {

            /// 1) log some logs with the logger
            /// 2) schedule a proper message to the client
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession,command.getCommandId())
            );
            return null;
        }

        // Getting the file from the current path which the client is in
        String filePathStr = command.getParam(0);
        Path filePath = Paths.get(filePathStr);

        String newFileName =
                command.getParamsCount() == 2 ? command.getParam(1) : filePath.getFileName().toString();

        long fileSize = Long.parseLong(command.getMetaData(0));
        String clientFilePath = command.getMetaData(1);

        File workingDir = clientSession.getClientProfile().getCurrentDir();
        File workingDirFile = new File(workingDir, newFileName);


        ///////////////////////////////////////////////////////////////////////////////////////////
        //  choose whether to upload the file or not (you can ignore files exceeding limit size here)
        //  or if there is restrictions on that client not to upload files
        long FILE_SIZE_LIMIT = (1 << 30); // 1GB
        //TODO: later each client will have a limit. there will a general limit for all also
        if(fileSize > FILE_SIZE_LIMIT) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.FILE_SIZE_LIMIT, clientSession, command.getCommandId())
            );
            return null;
        }
        ///////////////////////////////////////////////////////////////////////////////////////////



        // Send upload acknowledgement to client
        ReplyPacket replyPacket = ReplyType.UPLOAD_ACK.createPacket(command.getCommandId(), clientFilePath);
        SendPacketTask packetTask = new SendPacketTask(clientSession.getClientProfile(), replyPacket);
        TaskDispatcher.getInstance().submitSyncTask(packetTask);

        Server.serverLogger.info(String.format(
                "Receiving file at '%s' from \"%s\"",
                workingDirFile,
                clientSession.getClientProfile().getUserName()
        ));


        // Start receiving the file from the client
        ReceiveFileTask task = new ReceiveFileTask(clientSession.getClientProfile(), workingDirFile, fileSize, 0);
        TaskDispatcher.getInstance().submitAsyncTask(task);
        return null;
    }
}

