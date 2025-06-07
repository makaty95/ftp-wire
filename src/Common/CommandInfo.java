package Common;

import java.util.ArrayList;

public class CommandInfo {

    public String header;
    public String description;
    public String[] mParams;
    public String[] oParams;

    public CommandInfo(String header, String[] mParams,
                       String[] oParams ,String description) {
        this.header = header;
        this.description = description;
        this.mParams = mParams;
        this.oParams = oParams;
    }

    public CommandInfo(String header, String description) {
        this(header, new String[]{}, new String[]{}, description);
    }

    public int getMinArgsCount() {
        return mParams.length;
    }

    public int getMaxArgsCount() {
        return oParams.length + mParams.length;
    }

}
