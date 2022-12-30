package com.jbosframework.boot.autoconfig.quartz;

import com.jbosframework.boot.context.ConfigurationProperties;

@ConfigurationProperties(prefix = "jbos.quartz.threadPool")
public class QuartzProperties {
    private int threadCount;


}
