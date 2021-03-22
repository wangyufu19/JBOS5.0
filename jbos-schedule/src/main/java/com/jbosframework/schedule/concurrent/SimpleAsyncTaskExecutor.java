package com.jbosframework.schedule.concurrent;

import com.jbosframework.core.Order;

import java.util.concurrent.*;
/**
 * SimpleThreadPoolTaskExecutor
 * @author youfu.wang
 * @version 5.0
 */
public class SimpleAsyncTaskExecutor {
    private static int corePoolSize=10;
    private static int maxPoolSize= Order.MAX;
    private static long keepAliveSeconds=10;
    private static int queueCapacity=Order.MAX;

    private static class SimpleThreadPoolTaskExecutorHolder{
        private final static ExecutorService executor=new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueCapacity),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    public static ExecutorService getExecutorService() {
        return SimpleThreadPoolTaskExecutorHolder.executor;
    }
}
