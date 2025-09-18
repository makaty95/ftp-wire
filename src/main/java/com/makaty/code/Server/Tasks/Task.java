package com.makaty.code.Server.Tasks;


import java.util.concurrent.Callable;

@FunctionalInterface
public interface Task<T> extends Callable<T> {
    @Override
    T call() throws Exception;
}
