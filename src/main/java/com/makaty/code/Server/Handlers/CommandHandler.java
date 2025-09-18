package com.makaty.code.Server.Handlers;


import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Common.Models.Command;

import java.io.IOException;

@FunctionalInterface
public interface CommandHandler {
    Void handle(Command command, Session clientSession) throws IOException;
}
