package Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Common.Status;

/*
 * Tasks to be made by the server:
 * - send some reply message
 * - send data (files)
 * - 
 */
public class ThreadPool {

    public ExecutorService pool;

    public ThreadPool(int N) {
        pool = Executors.newFixedThreadPool(N);
    }
    
    // execute the task on worker threads.
    public Status ExecuteSch(Task task) {
        try {
            return pool.submit(task).get();

        } catch(Exception e) {
            System.err.println("Unhandled Exception in ThreadPool.java - 9300");
            return Status.CONNECTION_LOST;
        }

    }

    // execute the task without shceduling it on the threads.
    public Status ExecuteNow(Task task) {
        try {
            return task.call();

        } catch(Exception e) {
            System.err.println("Unhandled Exception in ThreadPool.java - 3829");
            return Status.CONNECTION_LOST;
        }
        

    }


    public void shutdown() {
        pool.shutdown(); // stop receiving any other tasks
    }
    
}
