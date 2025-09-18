package com.makaty.code.Client.Loggers;

import com.makaty.code.Common.Loggers.Logger;


public interface ClientLogger extends Logger {
    void RemoteLog(String message);
}
