package com.makaty.code.Server.Tasks;

import com.makaty.code.Server.Models.ClientProfile;
import com.makaty.code.Common.Models.Status;

public abstract class CommandTask implements Task<Status> {

    protected ClientProfile clientProfile;
    public CommandTask(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }
}
