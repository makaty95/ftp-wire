package com.makaty.code.Server.Handlers;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Models.Status;
import com.makaty.code.Common.Models.UtilityFunctions;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Common.Types.ReplyType;
import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Models.TaskDispatcher;
import com.makaty.code.Server.Models.Types.CommandType;
import com.makaty.code.Server.Models.Types.ErrorType;
import com.makaty.code.Server.Tasks.SendPacketTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListFilesCommandHandler implements CommandHandler {
    @Override
    public Void handle(Command command, Session clientSession) throws IOException {


        // 1) validate it is in a valid shape
        if(!CommandType.NLIST.isValidSignature(command)) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.INVALID_COMMAND_PARAMS, clientSession,command.getCommandId())
            );
            return null;
        }

        String dirName = command.getParam(0);
        File currentDir = clientSession.getClientProfile().getCurrentDir();

        File newFile = (dirName == null ? currentDir : UtilityFunctions.openDirectory(currentDir, dirName));

        /// SECURITY CHECK
        Status state = UtilityFunctions.checkFileAuthorization(newFile, clientSession.getClientProfile());
        if(state == Status.UNAUTHORIZED_ACCESS) {
            TaskDispatcher.getInstance().submitAsyncTask(() ->
                    new CommandErrorHandler().handle(ErrorType.UNAUTHORIZED, clientSession,command.getCommandId())
            );
            return null;
        }

        // 2) get all files inside the 'newFile'
        List<File> files = UtilityFunctions.getFilesInside(newFile);

        StringBuilder sb = new StringBuilder();
        for(File file : files) {
            String fileName = file.getName().concat((file.isDirectory() ? File.separator : ""));
            sb.append(fileName).append("\n");
        }

        // 3) send result to the client
        ReplyPacket replyPacket = ReplyType.MESSAGE.createPacket(command.getCommandId(),sb.toString());

        // submitting task to the dispatcher
        TaskDispatcher.getInstance().submitAsyncTask(new SendPacketTask(clientSession.getClientProfile(), replyPacket));
        return null;

    }
}
