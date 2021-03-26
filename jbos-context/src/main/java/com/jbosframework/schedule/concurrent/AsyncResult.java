package com.jbosframework.schedule.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * AsyncResult
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncResult<T> implements Future<T> {
    private final T Value;

    public AsyncResult(T Value) {
        this.Value=Value;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }


    public boolean isCancelled() {
        return false;
    }


    public boolean isDone() {
        return true;
    }


    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
