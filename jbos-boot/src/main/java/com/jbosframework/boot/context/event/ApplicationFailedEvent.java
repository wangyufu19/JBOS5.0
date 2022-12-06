package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.context.ConfigurableApplicationContext;

public class ApplicationFailedEvent extends JBOSApplicationEvent {

    private final ConfigurableApplicationContext context;

    private final Throwable exception;

    public ApplicationFailedEvent(JBOSApplication application, String[] args, ConfigurableApplicationContext context,
                                  Throwable exception) {
        super(application, args);
        this.context = context;
        this.exception = exception;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return this.context;
    }

    public Throwable getException() {
        return this.exception;
    }

}