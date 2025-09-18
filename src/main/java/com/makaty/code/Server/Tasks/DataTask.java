package com.makaty.code.Server.Tasks;

import com.makaty.code.Server.Models.ClientProfile;

public abstract class DataTask implements Task<Void> {

    protected final ClientProfile clientProfile;
    public DataTask(ClientProfile clientProfile) {
        this.clientProfile = clientProfile;
    }



}
