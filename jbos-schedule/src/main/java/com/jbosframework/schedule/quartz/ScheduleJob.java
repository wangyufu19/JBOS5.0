package com.jbosframework.schedule.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

/**
 * ScheduleJob
 * @author youfu.wang
 * @version 5.0
 */
public class ScheduleJob implements Job {
    private Object target;
    private Method method;

    public ScheduleJob(Object target, Method method){
        this.target=target;
        this.method=method;
    }
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
