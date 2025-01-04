package Common;

public enum Status {

    // connection
    CONNECTION_LOST,
    CONNECTION_ESTABLISHED,
    CLIENT_QUIT,

    // commands
    INVALID_COMMAND,
    INVALID_PARAMS_COUNT,
    REPLY_SENT,

    // files
    INVALID_PATH,
    FILE_SENT,

    // others
    SERIOUS_ERROR,
    STABLE;

    public static boolean isDisconnection(Status state) {
        return (state == CONNECTION_LOST || state == CLIENT_QUIT);
    }


    public static Status getStatus(String code) {
        return switch (code) {
            case "100" -> CONNECTION_ESTABLISHED;
            case "101" -> FILE_SENT;
            case "102" -> REPLY_SENT;
            default -> STABLE;
        };
    }


}
