package com.makaty.code.Server.Handlers;

import com.makaty.code.Server.Handshaking.Session;
import com.makaty.code.Server.Models.Types.ErrorType;


@FunctionalInterface
public interface ErrorHandler {
    Void handle(ErrorType errorType, Session session);
}
