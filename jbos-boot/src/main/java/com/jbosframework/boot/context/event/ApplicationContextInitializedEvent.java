package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.context.ConfigurableApplicationContext;

public class ApplicationContextInitializedEvent extends JBOSApplicationEvent {

    private final ConfigurableApplicationContext context;

    public ApplicationContextInitializedEvent(JBOSApplication application, String[] args,
                                              ConfigurableApplicationContext context) {
        super(application, args);
        this.context = context;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return this.context;
    }

}
