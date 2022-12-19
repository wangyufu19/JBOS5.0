package com.application.sys.service;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotifyListener implements ApplicationListener<NotifyEvent> {
    @Override
    public void onApplicationEvent(NotifyEvent event) {
        log.info(event.toString());
    }
}
