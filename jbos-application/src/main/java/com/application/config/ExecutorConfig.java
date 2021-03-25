package com.application.config;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.schedule.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * AsyncExecutorConfig
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
public class ExecutorConfig {
    @Value("${jbos.executor.corePoolSize}")
    private int corePoolSize;
    @Value("${jbos.executor.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jbos.executor.keepAliveSeconds}")
    private long keepAliveSeconds;
    @Value("${jbos.executor.queueCapacity}")
    private int queueCapacity;

    @Bean("asyncExecutor")
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}
