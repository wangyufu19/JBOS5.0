package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.core.env.ConfigurableEnvironment;

public class ApplicationEnvironmentPreparedEvent extends JBOSApplicationEvent {

    private final ConfigurableEnvironment environment;


    public ApplicationEnvironmentPreparedEvent(JBOSApplication application, String[] args,
                                               ConfigurableEnvironment environment) {
        super(application, args);
        this.environment = environment;
    }
    public ConfigurableEnvironment getEnvironment() {
        return this.environment;
    }

}
