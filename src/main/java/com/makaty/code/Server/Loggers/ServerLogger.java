package com.makaty.code.Server.Loggers;

import com.makaty.code.Common.Loggers.Logger;
import com.makaty.code.Server.Handshaking.Session;

public interface ServerLogger extends Logger {
    void reportClientConnection(Session session);
}
