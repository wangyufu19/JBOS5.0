package com.jbosframework.schedule.quartz;

import com.jbosframework.beans.factory.FactoryBean;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * SchedulerFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public class SchedulerFactoryBean implements FactoryBean<Scheduler> {
    private Scheduler scheduler;

    public SchedulerFactoryBean(){

    }
    public Scheduler getObject(){
        return this.scheduler;
    }

    public Class<?> getObjectType(){
        return this.scheduler!=null?this.scheduler.getClass():Scheduler.class;
    }

    public boolean isSingleton(){return true;}

    private void createScheduler(){
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            schedulerFactory.initialize();
            this.scheduler=schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    public Scheduler getScheduler(){
       if(this.scheduler==null){
           this.createScheduler();
       }
       return this.scheduler;
    }
}
