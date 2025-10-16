package com.makaty.code.Client.Models;

import com.makaty.code.Common.Types.ReplyType;

import java.util.List;

public class Reply {
    private ReplyType replyType;
    private List<Long> longs;
    private List<String> strings;
    private String commandId;

    public String getCommandId() {
        return commandId;
    }

    //TODO: (optimization) add int list also to handle all types of data being transferred

    public Reply(ReplyType replyType, List<Long> longs, List<String> strings,String commandId) {
        this.longs = longs;
        this.strings = strings;
        this.replyType = replyType;
        this.commandId = commandId;
    }

    public ReplyType getReplyType() {
        return replyType;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public List<String> getStrings() {
        return strings;
    }

}
