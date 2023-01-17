package com.application.test.listener;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("event={}",event);
    }
}
