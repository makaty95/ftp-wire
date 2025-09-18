package com.makaty.code.Server.Registries;

import com.makaty.code.Server.Models.Types.CommandType;

public class CommandRegistry {

    public static final String HELP_COMMANDS;

    public CommandRegistry() {}

    static {

        StringBuilder sb = new StringBuilder();
        sb.append("\n----------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-20s | %-30s | %-40s |\n","Command", "Parameters", "Description"));
        sb.append("----------------------------------------------------------------------------------------------------\n");

        int cnt = 1;
        for(CommandType com : CommandType.values()) {
            StringBuilder params = new StringBuilder();
            for(String param : com.getMandatoryParams()) {
                params.append(param);
                params.append(" ");
            }

            for(String param : com.getOptionalParams()) {
                params.append(param);
                params.append(" ");
            }

            sb.append(String.format("| %-20s | %-30s | %-40s |\n", "[" + cnt + "]" + com.getHeader(), params.toString(), com.getDescription()));

            cnt++;

        }

        sb.append("----------------------------------------------------------------------------------------------------\n");
        HELP_COMMANDS = sb.toString();

    }






}
