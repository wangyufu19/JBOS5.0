package com.jbosframework.schedule.concurrent;

import com.jbosframework.core.Ordered;

import java.util.concurrent.*;
/**
 * SimpleThreadPoolTaskExecutor
 * @author youfu.wang
 * @version 5.0
 */
public class SimpleAsyncTaskExecutor implements AsyncTaskExecutor {
    private static int corePoolSize=10;
    private static int maxPoolSize= Ordered.LOWEST_PRECEDENCE;
    private static long keepAliveSeconds=60;
    private static int queueCapacity= Ordered.LOWEST_PRECEDENCE;

    private static class SimpleThreadPoolTaskExecutorHolder{
        private final static Executor executor=new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueCapacity),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    public static Executor getExecutorService() {
        return SimpleThreadPoolTaskExecutorHolder.executor;
    }
    public void execute(Runnable task){
        SimpleAsyncTaskExecutor.getExecutorService().execute(task);
    }
    public Future<?> submit(Runnable task){
        FutureTask<Object> futureTask=new FutureTask(task,(Object) null);
        this.execute(futureTask);
        return futureTask;
    }

    public <T> Future<T> submit(Callable<T> task){
        FutureTask<T> futureTask=new FutureTask(task);
        this.execute(futureTask);
        return futureTask;
    }
}
