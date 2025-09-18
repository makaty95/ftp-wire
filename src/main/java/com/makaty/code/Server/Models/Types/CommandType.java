package com.makaty.code.Server.Models.Types;

import com.makaty.code.Server.Handlers.CommandHandler;
import com.makaty.code.Server.Handlers.HelpCommandHandler;
import com.makaty.code.Server.Handlers.QuitCommandHandler;
import com.makaty.code.Server.Handlers.RetrieveFileCommandHandler;
import com.makaty.code.Server.Exceptions.NoCommandWithSpecifiedHeaderException;
import com.makaty.code.Common.Models.Command;

import java.util.function.Supplier;


public enum CommandType {
    RETR(RetrieveFileCommandHandler::new,
            "retr", // header
            "gets a file specified with a path", // description
            new String[]{"file_path"}, // mandatory
            new String[]{"new_file_name"} // optional
    ),
    HELP(HelpCommandHandler::new,
            "help", // header
            "show the available commands to use", // description
            new String[]{}, // mandatory
            new String[]{} // optional
    ),
    QUIT(QuitCommandHandler::new,
            "quit", // header
            "quit the ftp serving process", // description
            new String[]{}, // mandatory
            new String[]{} // optional
    );

    private final Supplier<CommandHandler> commandHandlerSupplier;
    private final String header;
    private final String description;
    private final String[] mandatoryParams;
    private final String[] optionalParams;


    private int getMinArgsCount() {
        return mandatoryParams.length;
    }

    private int getMaxArgsCount() {
        return optionalParams.length + mandatoryParams.length;
    }


    public CommandHandler getCommandHandler() {
        return commandHandlerSupplier.get();
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public String[] getMandatoryParams() {
        return mandatoryParams;
    }

    public String[] getOptionalParams() {
        return optionalParams;
    }

    CommandType(Supplier<CommandHandler> commandHandlerSupplier,
                String header,
                String discription,
                String[] mandatoryParams,
                String[] optionalParams
    ) {
        this.header = header;
        this.mandatoryParams = mandatoryParams;
        this.optionalParams = optionalParams;
        this.description = discription;
        this.commandHandlerSupplier = commandHandlerSupplier;
    }

    public boolean isValidSignature(Command command) {
        boolean ok =
                command.getParamsCount() <= getMaxArgsCount() &&
                command.getParamsCount() >= getMinArgsCount();
        return ok;

    }

    public static CommandType typeOf(String header) throws NoCommandWithSpecifiedHeaderException {
        for(CommandType type : CommandType.values()) {
            if(type.getHeader().equals(header.toLowerCase())) {
                return type;
            }
        }
        throw new NoCommandWithSpecifiedHeaderException("Provided header: " + header);
    }

}