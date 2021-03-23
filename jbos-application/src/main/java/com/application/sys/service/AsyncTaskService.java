package com.application.sys.service;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.schedule.annotation.Async;
import com.jbosframework.schedule.concurrent.AsyncResult;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTaskService {
    @Async("asyncExecutor")
    public Future<Boolean> doTask1(int i)  {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******doTask1="+i);
        return new AsyncResult<Boolean>(true);
    }
    @Async
    public Future<Boolean> doTask2()  {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******doTask2");
        return new AsyncResult<Boolean>(true);
    }
    @Async
    public Future<Boolean> doTask3()  {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******doTask3");
        return new AsyncResult<Boolean>(true);
    }
}
