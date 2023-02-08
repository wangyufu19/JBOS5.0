package com.jbosframework.utils.thread;



import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolId = new AtomicInteger();
    private final AtomicInteger nextId;
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    protected final ThreadGroup threadGroup;

    public DefaultThreadFactory(Class<?> poolType) {
        this((Class)poolType, false, 5);
    }

    public DefaultThreadFactory(String poolName) {
        this((String)poolName, false, 5);
    }

    public DefaultThreadFactory(Class<?> poolType, boolean daemon) {
        this((Class)poolType, daemon, 5);
    }

    public DefaultThreadFactory(String poolName, boolean daemon) {
        this((String)poolName, daemon, 5);
    }

    public DefaultThreadFactory(Class<?> poolType, int priority) {
        this(poolType, false, priority);
    }

    public DefaultThreadFactory(String poolName, int priority) {
        this(poolName, false, priority);
    }

    public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
        this(toPoolName(poolType), daemon, priority);
    }

    public static String toPoolName(Class<?> poolType) {
        if (poolType == null) {
            throw new NullPointerException("poolType");
        } else {
            String poolName = poolType.getSimpleName();
            switch(poolName.length()) {
                case 0:
                    return "unknown";
                case 1:
                    return poolName.toLowerCase(Locale.US);
                default:
                    return Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1)) ? Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1) : poolName;
            }
        }
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup) {
        this.nextId = new AtomicInteger();
        if (poolName == null) {
            throw new NullPointerException("poolName");
        } else if (priority >= 1 && priority <= 10) {
            this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
            this.daemon = daemon;
            this.priority = priority;
            this.threadGroup = threadGroup;
        } else {
            throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
        this(poolName, daemon, priority, System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup());
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r,
                prefix + poolId.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}