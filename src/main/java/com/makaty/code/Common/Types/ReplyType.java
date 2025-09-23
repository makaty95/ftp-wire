package com.makaty.code.Common.Types;


import com.makaty.code.Client.ReplyHandlers.ReplyHandler;

import java.util.List;
import java.util.function.Supplier;

import com.makaty.code.Client.ReplyHandlers.*;
import com.makaty.code.Client.Models.ReplyPacketFactory;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Client.Models.Reply;

public enum ReplyType {

    FILE_INFO(FILE_INFO_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String fileName = (String) args[0];
        Long fileSize = (Long) args[1];
        return new ReplyPacket(new Reply(self, List.of(fileSize), List.of(fileName)));
    }),
    MESSAGE(MESSAGE_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String message = (String) args[0];
        return new ReplyPacket(new Reply(self, List.of(), List.of(message)));
    }),
    PWD_INFO(PWD_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String relativePath = (String) args[0];
        String absolutePath = (String) args[1];
        return new ReplyPacket(new Reply(self, List.of(), List.of(relativePath, absolutePath)));
    }),
    CWD_INFO(CWD_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String relativePath = (String) args[0];
        String absolutePath = (String) args[1];
        return new ReplyPacket(new Reply(self, List.of(), List.of(relativePath, absolutePath)));
    });

    private final Supplier<ReplyHandler> handlerSupplier;
    private final ReplyPacketFactory factory;
    ReplyType(Supplier<ReplyHandler> handlerSupplier, ReplyPacketFactory factory) {
        this.handlerSupplier = handlerSupplier;
        this.factory = factory;


    }

    public ReplyHandler getReplyHandler() {
        return handlerSupplier.get();
    }

    public ReplyPacket createPacket(Object... args) {
        return factory.createPacket(this, args);
    }
}
