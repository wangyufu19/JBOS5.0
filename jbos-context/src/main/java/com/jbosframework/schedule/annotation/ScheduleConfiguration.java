package com.jbosframework.schedule.annotation;

import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.support.BeanProcessorRegistry;
import com.jbosframework.schedule.quartz.ScheduleBeanProcessor;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * ScheduleConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
public class ScheduleConfiguration extends BeanProcessorRegistry {
    private Log log= LogFactory.getLog(ScheduleConfiguration.class);

    public  void registryBeanProcessor(){
        SchedulerFactoryBean schedulerFactoryBean=new SchedulerFactoryBean();
        Scheduler scheduler=schedulerFactoryBean.getScheduler();
        if(scheduler!=null){
            try {
                scheduler.start();
                log.info("******启动Quartz Scheduler成功");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(Scheduler.class.getName(),Scheduler.class);
            this.getBeanFactory().putBeanDefinition(annotationBean);
            this.getBeanFactory().putBean(Scheduler.class.getName(),scheduler);
            ScheduleBeanProcessor scheduleBeanProcessor=new ScheduleBeanProcessor(this.getBeanFactory());
            scheduleBeanProcessor.setOrder(60);
            this.getBeanFactory().addBeanPostProcessor(scheduleBeanProcessor);
        }
    }
}
