package com.makaty.Server;

import com.makaty.code.Common.Models.Command;
import com.makaty.code.Server.Exceptions.NoCommandWithSpecifiedHeaderException;
import com.makaty.code.Server.Models.Types.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerModule {

    @Test
    public void testCommandValidation1() {
        Command command = new Command("retr");
        CommandType type;
        try {
            type = CommandType.typeOf(command);
        } catch (NoCommandWithSpecifiedHeaderException e) {
            throw new RuntimeException(e);
        }
        assertEquals(CommandType.RETR, type);
    }

    @Test
    public void testCommandValidation2() {
        Command command = new Command("retrt");
        CommandType type;
        try {
            type = CommandType.typeOf(command);
        } catch (NoCommandWithSpecifiedHeaderException e) {
            return;
        }
        throw new RuntimeException();
    }
}
