package com.jbosframework.boot.autoconfig.quartz;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnMissingBean;
import com.jbosframework.boot.context.EnableConfigurationProperties;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;

@Configuration
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SchedulerFactoryBean.class)
    public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties){
        return new SchedulerFactoryBean();
    }
}
