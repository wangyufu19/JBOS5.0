package com.application.test.async;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.schedule.annotation.async.Async;
import com.jbosframework.schedule.annotation.Scheduled;
import com.jbosframework.schedule.concurrent.AsyncResult;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTaskService {
    @Async("asyncExecutor")
    public Future<Boolean> doTask1()  {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******doTask1");
        return new AsyncResult<Boolean>(true);
    }
    @Async
    @Scheduled
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******doTask3");
        return new AsyncResult<Boolean>(true);
    }
}
