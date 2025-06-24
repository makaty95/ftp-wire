package Server;

import Common.Status;
import java.util.concurrent.Callable;

public abstract class Task implements Callable<Status> {

    private static int counter = 0;
    private int id;

    public Task(){this.id = ++counter;}
    public int getID(){return id;}

}

