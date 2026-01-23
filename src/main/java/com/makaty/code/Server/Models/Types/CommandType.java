package com.makaty.code.Server.Models.Types;

import com.makaty.code.Common.Models.UtilityFunctions;
import com.makaty.code.Server.Handlers.*;
import com.makaty.code.Common.Exceptions.NoCommandWithSpecifiedHeaderException;
import com.makaty.code.Common.Models.Command;

import java.util.List;
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
    ),
    CWD(ChangeWorkingDirectoryHandler::new,
            "cwd", // header
            "change working directory", // description
            new String[]{"directory_name"}, // mandatory
            new String[]{} // optional
    ),
    PWD(PrintWorkingDirectoryHandler::new,
            "pwd", // header
            "get info about the current working directory", // description
            new String[]{}, // mandatory
            new String[]{} // optional
    ),
    NLIST(ListFilesCommandHandler::new,
            "nlist", // header
            "list all files and folders inside a directory", // description
            new String[]{}, // mandatory
            new String[]{"directory_name"} // optional
    ),
    STOR(StoreFileCommandHandler::new,
            "stor", // header
            "uploads a file to the remote", // description
            new String[]{"file_path"}, // mandatory
            new String[]{"new_file_name"} // optional
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



    private static String buildCommandsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-20s | %-30s | %-40s |\n", "Command", "Parameters", "Description"));
        sb.append("----------------------------------------------------------------------------------------------------\n");

        int cnt = 1;
        for (CommandType com : CommandType.values()) {
            // Build params string
            StringBuilder params = new StringBuilder();
            for (String param : com.mandatoryParams) {
                params.append("<").append(param).append("> ");
            }
            for (String param : com.optionalParams) {
                params.append("(").append(param).append(") ");
            }

            // Wrap params and description
            List<String> wrappedParams = UtilityFunctions.wrap(params.toString().trim(), 30);
            List<String> wrappedDesc = UtilityFunctions.wrap(com.description, 40);

            // Get max number of lines needed for this row
            int maxLines = Math.max(wrappedParams.size(), wrappedDesc.size());

            // Print row(s)
            for (int i = 0; i < maxLines; i++) {
                String commandCol = (i == 0 ? "[" + cnt + "]" + com.header : "");
                String paramCol   = (i < wrappedParams.size() ? wrappedParams.get(i) : "");
                String descCol    = (i < wrappedDesc.size() ? wrappedDesc.get(i) : "");

                sb.append(String.format("| %-20s | %-30s | %-40s |\n", commandCol, paramCol, descCol));
            }

            cnt++;
        }

        sb.append("----------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }
    public static final String COMMANDS_INFO = buildCommandsInfo();
    private static String getCommandsInfo() {return COMMANDS_INFO;}



    public boolean isValidSignature(Command command)  {
        boolean ok =
                command.getParamsCount() <= getMaxArgsCount() &&
                command.getParamsCount() >= getMinArgsCount();
        return ok;

    }

    public static CommandType typeOf(Command command) throws NoCommandWithSpecifiedHeaderException {
        for(CommandType type : CommandType.values()) {
            if(type.header.equals(command.getHeader().toLowerCase())) {
                return type;
            }
        }
        throw new NoCommandWithSpecifiedHeaderException("Provided header: " + command.getHeader());
    }

}