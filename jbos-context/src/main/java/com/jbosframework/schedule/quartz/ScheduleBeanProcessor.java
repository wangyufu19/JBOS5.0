package com.jbosframework.schedule.quartz;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.core.Order;
import com.jbosframework.schedule.annotation.Scheduled;

import java.lang.reflect.Method;

import org.quartz.*;

/**
 * ScheduleBeanProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class ScheduleBeanProcessor implements BeanPostProcessor {
    private BeanFactory beanFactory;
    private int order= Order.MIN;

    public ScheduleBeanProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return this.order;
    }
    public int compareTo(BeanPostProcessor beanPostProcessor) {
        return this.order - beanPostProcessor.getOrder();
    }
    public Object process(Object obj) {
        Object target = obj;
        Class<?> cls = obj.getClass();
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
                ScheduleJob scheduleJob = new ScheduleJob(target, method);
                JobDetail job = JobBuilder.newJob(ScheduleJob.class)
                        .withIdentity(cls.getName(), "group1")
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(cls.getName(), "group1")
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .forJob(job.getKey())
                        .build();
                Scheduler scheduler=this.beanFactory.getBean(Scheduler.class);
                if(scheduler!=null){
                    try {
                        scheduler.scheduleJob(job,trigger);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }
}
