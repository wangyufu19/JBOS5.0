package com.application.config;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.quartz.QuartzProperties;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.schedule.annotation.EnableScheduling;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduleConfig
 * cron 表达式只能写6个，顺序：秒 分 时 日 月 周  ？
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties){
        return new SchedulerFactoryBean();
    }

}
