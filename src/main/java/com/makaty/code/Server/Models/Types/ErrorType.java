package com.makaty.code.Server.Models.Types;

public enum ErrorType {
    INVALID_COMMAND("Invalid command format.", "Invalid command."),
    INVALID_COMMAND_PARAMS("Invalid command arguments", "Invalid command args count or type."),
    INVALID_PATH("Invalid file path provided.", "The file path you requested does not exist."),
    UNAUTHORIZED("Unauthorized request.", "You are not allowed to perform this action."),
    INTERNAL_ERROR("Internal server error.", "An unexpected error occurred on the server.");

    private final String logMessage;
    private final String clientMessage;

    ErrorType(String logMessage, String clientMessage) {
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}