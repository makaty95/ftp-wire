package com.makaty.code.Client.ReplyHandlers;

import com.makaty.code.Client.Models.Reply;

@FunctionalInterface
public interface ReplyHandler {
    void handle(Reply reply);
}
