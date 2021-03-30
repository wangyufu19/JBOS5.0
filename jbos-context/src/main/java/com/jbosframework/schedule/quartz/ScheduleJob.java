package com.jbosframework.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ScheduleJob
 * @author youfu.wang
 * @version 5.0
 */
public class ScheduleJob implements Job {

    public ScheduleJob(){

    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map dataMap = context.getJobDetail().getJobDataMap();
        Object target=dataMap.get("target");
        Method method=(Method)dataMap.get("method");
        try {
            method.invoke(target,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
