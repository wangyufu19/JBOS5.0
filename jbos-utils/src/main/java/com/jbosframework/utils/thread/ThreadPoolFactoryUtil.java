package com.jbosframework.utils.thread;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class ThreadPoolFactoryUtil {
    private static final AtomicInteger poolId = new AtomicInteger();
    private final AtomicInteger nextId;
    /**
     * 通过 threadNamePrefix 来区分不同线程池（我们可以把相同 threadNamePrefix 的线程池看作是为同一业务场景服务）。
     * key: threadNamePrefix
     * value: threadPool
     */
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtil() {
        this.nextId = new AtomicInteger();
    }

    public static ExecutorService createThreadPoolIfAbsent(String threadNamePrefix) {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        return createThreadPoolIfAbsent(threadPoolConfig, threadNamePrefix);
    }

    public static ExecutorService createThreadPoolIfAbsent(ThreadPoolConfig threadPoolConfig, String threadNamePrefix) {
        threadPoolConfig.setPoolName(threadNamePrefix);
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadPoolConfig));
        // 如果 threadPool 被 shutdown 的话就重新创建一个
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = createThreadPool(threadPoolConfig);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }

    /**
     * shutDown 所有线程池
     */
    public static void shutDownAllThreadPool() {
        log.info("call shutDownAllThreadPool method");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            log.info("shut down thread pool [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Thread pool never terminated");
                executorService.shutdownNow();
            }
        });
    }

    private static ExecutorService createThreadPool(ThreadPoolConfig threadPoolConfig) {
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaxPoolSize(),
                threadPoolConfig.getKeepAliveTime(), threadPoolConfig.getUnit(), threadPoolConfig.getWorkQueue(),
                threadPoolConfig.getThreadFactory());
    }

    /**
     * 创建 ThreadFactory 。如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     *
     * @param threadNamePrefix 作为创建的线程名字的前缀
     * @param daemon           指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, boolean daemon) {
        threadNamePrefix = threadNamePrefix + '-' + poolId.incrementAndGet() + '-';
        if (threadNamePrefix != null) {
            if (daemon) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(true).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池对象
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("============ThreadPool Status=============");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            log.info("Number of Tasks : [{}]", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("===========================================");
        }, 0, 1, TimeUnit.MINUTES);
    }
}
