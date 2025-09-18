package com.makaty.code.Server.Models;


import com.makaty.code.Server.Tasks.Task;

public class TaskDispatcher {
    private static final ThreadPool threadPool = new ThreadPool(ServerConfig.WORKER_THREADS);
    private static volatile TaskDispatcher instance;

    private TaskDispatcher(){}



    public static TaskDispatcher getInstance() {
        if (instance == null) {
            synchronized (TaskDispatcher.class) {
                if (instance == null) {
                    instance = new TaskDispatcher();
                }
            }
        }
        return instance;
    }


    public <T> T submitSyncTask(Task<T> task) {
        return threadPool.ExecuteSync(task);
    }

    public void submitAsyncTask(Task<?> task) {
        threadPool.ExecuteAsync(task);
    }

    public void shutdown() {
        threadPool.shutdown();
    }

}
