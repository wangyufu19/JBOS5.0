package com.jbosframework.boot.autoconfig.schedule;

import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.schedule.annotation.EnableScheduling;
import com.jbosframework.schedule.quartz.ScheduleBeanProcessor;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;


/**
 * ScheduleAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableScheduling
public class ScheduleAutoConfiguration extends AbstractAutoConfiguration {
    private Log log= LogFactory.getLog(ScheduleAutoConfiguration.class);
    /**
     * 注册自动配置组件到容器中
     * @return
     */
    public void registry(){
        if (!this.conditionalOnConfiguration(this.getClass())) {
            return;
        }
        EnableScheduling enableScheduling=this.getClass().getDeclaredAnnotation(EnableScheduling.class);
        if(enableScheduling==null){
            return;
        }
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
            this.getApplicationContext().putBeanDefinition(annotationBean);
            this.getApplicationContext().putBean(Scheduler.class.getName(),scheduler);
            ScheduleBeanProcessor scheduleBeanProcessor=new ScheduleBeanProcessor(this.getApplicationContext());
            scheduleBeanProcessor.setOrder(60);
            this.getApplicationContext().addBeanPostProcessor(scheduleBeanProcessor);
        }
    }
}
