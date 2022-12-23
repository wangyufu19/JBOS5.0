package com.jbosframework.schedule.annotation;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;
import com.jbosframework.schedule.quartz.ScheduleBeanPostProcessor;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * ScheduleRegister
 * @author youfu.wang
 * @version 5.0
 */

public class ScheduleRegister implements ImportSelector {
    private ConfigurableApplicationContext applicationContext;

    public ScheduleRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        SchedulerFactoryBean schedulerFactoryBean=new SchedulerFactoryBean();
        Scheduler scheduler=schedulerFactoryBean.getScheduler();
        if(scheduler!=null) {
            try {
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(Scheduler.class);
            genericBeanDefinition.setRole(GenericBeanDefinition.ROLE_APPLICATION);
            this.applicationContext.getBeanFactory().putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
            this.applicationContext.getBeanFactory().registerSingletonInstance(Scheduler.class.getName(),scheduler);
            ScheduleBeanPostProcessor scheduleBeanPostProcessor=new ScheduleBeanPostProcessor(this.applicationContext);
            this.applicationContext.getBeanFactory().registerBeanPostProcessor(scheduleBeanPostProcessor);
        }
        return null;
    }
}
