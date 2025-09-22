package com.makaty.code.Common.Exceptions;

import java.io.IOException;

public class CommandFormatException extends IOException {
    public CommandFormatException(String message) {
        super(message);
    }
}
