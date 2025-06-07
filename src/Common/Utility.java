package Common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class Utility {

    public static final Command byeCommand = new Command("bye");
    public static HashMap<String, CommandInfo> commands;

    public static void initializeCommands() {
        // put commands to the map
        commands = new HashMap<>();
        commands.put("get_file",
                new CommandInfo("get_file",
                new String[]{"[file_path]"},
                new String[]{"<new_file_name>"},
                "gets a file specified with a path"));
        commands.put("bye", new CommandInfo("bye", "quit the ftp serving process"));
        commands.put("cmds", new CommandInfo("cmds", "show the available commands to use"));

    }

    public static Command toCommand(byte[] buffer) {
        String str = new String(buffer).trim();
        System.out.println("<Command>: " + str);
        String[] splits = str.split(" ");
        Command ret = new Command(splits[0]);
        for(int i = 1; i<splits.length; i++) {
            ret.addParam(splits[i]);
        }

        return ret;
    }

    public static Reply toReply(byte[] buffer) {
        String content = new String(buffer).trim();
        String[] splits = content.split("#");

        return new Reply(splits[0], splits[1]);

    }

    public static String toMessage(Reply reply) {
        StringBuilder ret = new StringBuilder();
        ret.append("#");
        ret.append(reply.getCode());
        ret.append("#");
        ret.append(reply.getContent());
        ret.append("#");
        return ret.toString();
    }


    public static Status isValidCommand(Command command) {
        String header = command.getHeader();

        // if command not exist
        if(!commands.containsKey(header)) return Status.INVALID_COMMAND;

        int minArgsCount =  commands.get(command.getHeader()).getMinArgsCount();
        int maxArgsCount =  commands.get(command.getHeader()).getMaxArgsCount();
        int paramsCount = command.getParamsCount();

        // if params count is invalid
        if(paramsCount < minArgsCount || paramsCount > maxArgsCount) return Status.INVALID_PARAMS_COUNT;

        return Status.STABLE;

    }

    public static boolean isConnectionReply(Reply reply) {
        return reply.getCode().equals("103");
    }


}
