package com.makaty.code.clientCLI;

import com.makaty.code.Common.Exceptions.CommandFormatException;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Models.UtilityFunctions;
import com.makaty.code.Common.Exceptions.NoCommandWithSpecifiedHeaderException;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.List;

public enum OfflineCommandType {

    SET_USERNAME(
        (command, client, terminal) -> {
            String name = command.getParam(0);
            client.setUserName(name);
        },
        "set_username",
        "set your username which will be sent to the remote.",
        new String[]{"new_username"},
        new String[]{}
    ),

    SET_REMOTE(
        (command, client, terminal) -> {

            try {
                String hostname = command.getParam(0);
                int port = Integer.parseInt(command.getParam(1));
                client.setRemoteHost(hostname, port);
            } catch (NumberFormatException e) {
                terminal.writer().println("Invalid port - enter an integer.");
                terminal.writer().flush();
            }
        },
        "set_remote",
        "set the hostname and port of the remote.",
        new String[]{"remote_hostname", "remote_port"},
        new String[]{}
    ),
    BYE(
            (command, client, terminal) -> {

                try {
                    if (client.isConnected()) {
                        client.terminateConnection();
                    }
                    terminal.writer().println("See you next time.");
                    terminal.writer().flush();
                    Thread.sleep(1000);
                    System.exit(0);
                } catch (Exception e) {
                    System.exit(0);
                }
            },
            "bye",
            "close connection.",
            new String[]{},
            new String[]{}
    ),

    SET_LOCAL(
        (command, client, terminal) -> {

            try {
                String hostname = command.getParam(0);
                int port = Integer.parseInt(command.getParam(1));
                client.setLocalHost(hostname, port);
            } catch (NumberFormatException e) {
                terminal.writer().println("Invalid port - enter an integer.");
                terminal.writer().flush();
            }
        },
        "set_local",
        "set the hostname and port of your machine.",
        new String[]{"local_hostname", "local_port"},
        new String[]{}
    ),

    INIT_CONNECTION(
        (command, client, terminal) -> {
            try {
                client.initConnection();
            } catch (NumberFormatException e) {
                terminal.writer().println("Invalid port - enter an integer.");
                terminal.writer().flush();
            }
        },
        "connect",
        "initialize the connection between your machine and the remote.",
        new String[]{},
        new String[]{}
    ),

    CLEAR_SCREEN(
        (command, client, terminal) -> {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
        },
        "clear",
        "clears your terminal screen.",
        new String[]{},
        new String[]{}
    ),

    CONN_INFO(
            (command, client, terminal) -> {
                StringBuilder sb = new StringBuilder();

                // remote
                sb.append("Remote:\n").append("\t IP: ").append(client.getRemoteHost()).append("\n");
                sb.append("\t PORT: ").append(client.getRemotePort()).append("\n");

                // local
                sb.append("Local:\n").append("\t IP: ").append(client.getLocalHost()).append("\n");
                sb.append("\t PORT: ").append(client.getLocalPort()).append("\n");
                sb.append("\t Username: ").append(client.getUserName()).append("\n");

                terminal.writer().write(sb.toString());
                terminal.flush();
            },
            "conn_info",
            "display info about both local and remote connection.",
            new String[]{},
            new String[]{}
    ),

    HELP(
            (command, client, terminal) -> {
                terminal.writer().print(OfflineCommandType.getCommandsInfo());
                terminal.writer().flush();
            },
            "help",
            "display commands info list.",
            new String[]{},
            new String[]{}
    );

    public static final String COMMANDS_INFO = buildCommandsInfo();
    private static String getCommandsInfo() {
        return COMMANDS_INFO;
    }

//    private static String buildCommandsInfo() {
//
//
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("\n----------------------------------------------------------------------------------------------------\n");
//        sb.append(String.format("| %-20s | %-30s | %-40s |\n","Command", "Parameters", "Description"));
//        sb.append("----------------------------------------------------------------------------------------------------\n");
//
//        int cnt = 1;
//        for(OfflineCommandType com : OfflineCommandType.values()) {
//            StringBuilder params = new StringBuilder();
//            for(String param : com.mandatoryParams) {
//                params.append(param);
//                params.append(" ");
//            }
//
//            for(String param : com.optionalParams) {
//                params.append(param);
//                params.append(" ");
//            }
//
//            sb.append(String.format("| %-20s | %-30s | %-40s |\n", "[" + cnt + "]" + com.commandHeader, params.toString(), com.description));
//
//            cnt++;
//
//        }
//
//        sb.append("----------------------------------------------------------------------------------------------------\n");
//        return sb.toString();
//
//    }

    private static String buildCommandsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-20s | %-30s | %-40s |\n", "Command", "Parameters", "Description"));
        sb.append("----------------------------------------------------------------------------------------------------\n");

        int cnt = 1;
        for (OfflineCommandType com : OfflineCommandType.values()) {
            // Build params string
            StringBuilder params = new StringBuilder();
            for (String param : com.mandatoryParams) {
                params.append(param).append(" ");
            }
            for (String param : com.optionalParams) {
                params.append(param).append(" ");
            }

            // Wrap params and description
            List<String> wrappedParams = UtilityFunctions.wrap(params.toString().trim(), 30);
            List<String> wrappedDesc = UtilityFunctions.wrap(com.description, 40);

            // Get max number of lines needed for this row
            int maxLines = Math.max(wrappedParams.size(), wrappedDesc.size());

            // Print row(s)
            for (int i = 0; i < maxLines; i++) {
                String commandCol = (i == 0 ? "[" + cnt + "]" + com.commandHeader : "");
                String paramCol   = (i < wrappedParams.size() ? wrappedParams.get(i) : "");
                String descCol    = (i < wrappedDesc.size() ? wrappedDesc.get(i) : "");

                sb.append(String.format("| %-20s | %-30s | %-40s |\n",
                        commandCol, paramCol, descCol));
            }

            cnt++;
        }

        sb.append("----------------------------------------------------------------------------------------------------\n");
        return sb.toString();
    }




    private final String commandHeader;
    private final String[] mandatoryParams, optionalParams;
    private final String description;
    private final CommandHandler commandHandler;

    OfflineCommandType(
            CommandHandler commandHandler,
            String header,
            String description,
            String[] mandatoryParams,
            String[] optionalParams
            ) {
        this.commandHandler = commandHandler;
        this.commandHeader = header;
        this.description = description;
        this.mandatoryParams = mandatoryParams;
        this.optionalParams = optionalParams;
    }

    public CommandHandler getCommandHandler(){return commandHandler;}

    private int getMaxArgsCount(){return this.mandatoryParams.length + this.optionalParams.length;}
    private int getMinArgsCount(){return this.mandatoryParams.length;}

    private boolean isValidSignature(Command command) {
        boolean ok =
                command.getParamsCount() <= getMaxArgsCount() &&
                command.getParamsCount() >= getMinArgsCount();
        return ok;

    }

    private static OfflineCommandType typeOf(Command command) throws NoCommandWithSpecifiedHeaderException {
        for(OfflineCommandType cmd : OfflineCommandType.values()) {
            if(cmd.commandHeader.equals(command.getHeader())) {
                return cmd;
            }
        }

        throw new NoCommandWithSpecifiedHeaderException(String.format("'%s' is not recognized as an internal or external command", command.getHeader()));

    }

    public static OfflineCommandType validateCommand(Command command) throws NoCommandWithSpecifiedHeaderException, CommandFormatException {

        OfflineCommandType com = typeOf(command);
        if(com.isValidSignature(command)) return com;

        String message = "Command format is invalid.\ntype 'help' to show commands usage.";
        throw new CommandFormatException(message);
    }

}
