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
    STABLE,
    CONNECTION_TERMINATED;

    public static boolean isDisconnection(Status state) {
        return (state == CONNECTION_LOST || state == CLIENT_QUIT);
    }


    public static Status getStatus(String code) {
        switch (code) {
            case "100":
                return CONNECTION_ESTABLISHED;
            case "101":
                return FILE_SENT;
            case "102":
                return REPLY_SENT;
            default:
                return STABLE;
        }
    }


}
