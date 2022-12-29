package com.jbosframework.schedule.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import java.lang.reflect.Method;
import java.util.Map;
import com.jbosframework.schedule.annotation.async.AsyncBeanPostProcessor;
import com.jbosframework.schedule.quartz.ScheduleJob;
import com.jbosframework.schedule.quartz.SchedulerFactoryBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.StdScheduler;

/**
 * ScheduleBeanProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class ScheduleBeanPostProcessor implements BeanPostProcessor,Ordered {
    private static final Log logger= LogFactory.getLog(AsyncBeanPostProcessor.class);
    private ConfigurableApplicationContext applicationContext;
    private int order=Ordered.HIGHEST_PRECEDENCE+80;

    public ScheduleBeanPostProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public int getOrder() {
        return this.order;
    }

    public Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        Object target = bean;
        Class<?> cls = bean.getClass();
        if (cls == null) {
            return target;
        }

        Method[] methods = cls.getDeclaredMethods();
        if (methods == null) {
            return target;
        }
        for (Method method : methods) {
            Scheduled scheduled = method.getDeclaredAnnotation(Scheduled.class);
            if (scheduled != null) {
                String cron=scheduled.cron();
                JobDetail job = JobBuilder.newJob(ScheduleJob.class)
                        .withIdentity(cls.getName(), cls.getName())
                        .build();
                Map dataMap = job.getJobDataMap();
                dataMap.put("target",target);
                dataMap.put("method",method);

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(cls.getName(), cls.getName())
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .forJob(job.getKey())
                        .build();

                Scheduler scheduler=(Scheduler)this.applicationContext.getBean(SchedulerFactoryBean.class.getName());

                if(scheduler!=null){
                    try {
                        scheduler.scheduleJob(job,trigger);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return bean;
    }
}
