package com.makaty.code.Server.Models;

import com.makaty.code.Server.Server;
import com.makaty.code.Server.Tasks.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/*
 * Tasks to be made by the server:
 * - send some message via the command socket.
 * - send data (files) via the data socket.
 * - 
 */
public class ThreadPool {

    private ExecutorService pool;

    public ThreadPool(int N) {
        pool = Executors.newFixedThreadPool(N);
    }
    
    // execute the task on worker threads.
    public <T> T ExecuteSync(Task<T> task) {
        try {
            return pool.submit(task).get();
        } catch (RejectedExecutionException e) {
            Server.serverLogger.error("Thread pool rejected task: " + task);
            return null;
        } catch (InterruptedException e) {
            Server.serverLogger.error("Thread pool interrupted, message: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } catch (ExecutionException e) {
            Server.serverLogger.error("Thread pool execution error, cause: " + e.getCause());
            return null;
        } catch (Exception e) {

            Server.serverLogger.error("Some Sync. task could not be scheduled in the Thread pool!");
            return null;
        }

    }

    public void ExecuteAsync(Task<?> task) {
        try {
            pool.submit(task); // schedule task, return immediately
        } catch(Exception e) {
            e.printStackTrace();
            Server.serverLogger.error("Some Async. task could not be scheduled in the Thread pool!");
        }
    }

    public void shutdown() {
        pool.shutdown(); // stop receiving any other tasks
    }
    
}
