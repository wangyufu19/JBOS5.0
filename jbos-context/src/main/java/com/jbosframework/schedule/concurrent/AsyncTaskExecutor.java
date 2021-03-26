package com.jbosframework.schedule.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * AsyncTaskExecutor
 * @author youfu.wang
 * @version 5.0
 */
public interface AsyncTaskExecutor extends Executor {
    Future<?> submit(Runnable task);

    <T> Future<T> submit(Callable<T> task);

}
