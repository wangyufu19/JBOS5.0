package com.application.config;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.schedule.annotation.Async;

@Component
public class AsyncTaskConfig {
    @Async
    public void saveTrace(){

    }
}
