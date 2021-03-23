package com.jbosframework.schedule.concurrent;

import com.jbosframework.core.Order;

import java.util.concurrent.*;

/**
 * ThreadPoolTaskExecutor
 * @author youfu.wang
 * @version 5.0
 */
public class ThreadPoolTaskExecutor implements AsyncTaskExecutor{
    private int corePoolSize=1;
    private int maxPoolSize= Order.MAX;
    private long keepAliveSeconds=60;
    private int queueCapacity=Order.MAX;
    private Executor executor;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
    public void initialize(){
        BlockingQueue<Runnable> queue=new LinkedBlockingQueue<Runnable>(this.queueCapacity);
        executor=new ThreadPoolExecutor(
                this.corePoolSize,
                this.maxPoolSize,
                this.keepAliveSeconds,
                TimeUnit.SECONDS,
                queue,
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    public void execute(Runnable task){
        executor.execute(task);
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
