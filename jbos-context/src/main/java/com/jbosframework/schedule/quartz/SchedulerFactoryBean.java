package com.jbosframework.schedule.quartz;

import com.jbosframework.beans.factory.FactoryBean;
import com.jbosframework.beans.factory.InitializingBean;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

/**
 * SchedulerFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public class SchedulerFactoryBean implements InitializingBean,FactoryBean<Scheduler> {

    private Scheduler scheduler;

    public SchedulerFactoryBean(){

    }
    public Object afterPropertiesSet(Object obj) {
        this.initialize();
        if(scheduler!=null){
            try {
                scheduler.start();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
    public Scheduler getObject(){
        return this.scheduler;
    }

    public Class<?> getObjectType(){
        return this.scheduler!=null?this.scheduler.getClass():Scheduler.class;
    }

    public boolean isSingleton(){return true;}

    private void initialize(){
        Properties properties=new Properties();
        try {
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            schedulerFactory.initialize();
            this.scheduler=schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
