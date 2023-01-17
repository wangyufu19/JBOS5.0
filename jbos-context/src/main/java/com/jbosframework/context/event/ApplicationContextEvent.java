package com.jbosframework.context.event;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationEvent;

public class ApplicationContextEvent  extends ApplicationEvent {
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext)this.getSource();
    }
}

