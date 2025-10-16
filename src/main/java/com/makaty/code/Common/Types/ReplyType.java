package com.makaty.code.Common.Types;


import com.makaty.code.Client.ReplyHandlers.ReplyHandler;

import java.util.List;
import java.util.function.Supplier;

import com.makaty.code.Client.ReplyHandlers.*;
import com.makaty.code.Client.Models.ReplyPacketFactory;
import com.makaty.code.Common.Packets.Communication.ReplyPacket;
import com.makaty.code.Client.Models.Reply;

public enum ReplyType {
    //update factory lambdas to accept commandId as an optional first argument
    //Optional: ReplyPacket reply = ReplyType.MESSAGE.createPacket(command.getId(), "File uploaded successfully!");
    FILE_INFO(FILE_INFO_ReplyHandler::new, (ReplyType self, Object... args) -> {
       //  String commandId = (String) args[0];
//temporary correction, createPacket() usage have to change everywhere because of the added argument
        String commandId = args.length>2 ? (String)args[0] : null;
        String fileName =args.length>2? (String) args[1]:(String) args[0];
        Long fileSize = args.length>2?(Long) args[2]:(Long) args[1];
        return new ReplyPacket(new Reply(self, List.of(fileSize), List.of(fileName),commandId));
    }),
    MESSAGE(MESSAGE_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String commandId =args.length>1? (String) args[0]:null;
        String message =args.length>1? (String) args[1]:(String) args[0];
        return new ReplyPacket(new Reply(self, List.of(), List.of(message),commandId));
    }),
    PWD_INFO(PWD_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String commandId =args.length>2? (String) args[0]:null;
        String relativePath =args.length>2? (String) args[1]:(String) args[0];
        String absolutePath =args.length>2?  (String) args[2]:(String) args[1];
        return new ReplyPacket(new Reply(self, List.of(), List.of(relativePath, absolutePath),commandId));
    }),
    CWD_INFO(CWD_ReplyHandler::new, (ReplyType self, Object... args) -> {
        String commandId =args.length>2? (String) args[0]:null;
        String relativePath =args.length>2?  (String) args[1]:(String) args[0];
        String absolutePath = args.length>2? (String) args[2]:(String) args[1];
        return new ReplyPacket(new Reply(self, List.of(), List.of(relativePath, absolutePath),commandId));
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
