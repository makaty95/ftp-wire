package com.makaty.code.Common.Exceptions;

import java.io.IOException;

public class RemoteDisconnectionException extends IOException {
    public RemoteDisconnectionException(String message) {
        super(message);
    }
}
