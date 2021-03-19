package com.application.config;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.schedule.annotation.Async;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AsyncTaskConfig {
    @Async
    public void saveTrace()  {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("******Async task saveTrace");
    }
}
