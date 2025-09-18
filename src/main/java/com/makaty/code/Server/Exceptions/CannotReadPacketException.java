package com.makaty.code.Server.Exceptions;

import java.io.IOException;

public class CannotReadPacketException extends IOException {
    public CannotReadPacketException(String message) {super(message);}
}
