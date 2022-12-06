package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.context.ApplicationEvent;

public class JBOSApplicationEvent extends ApplicationEvent {
    private final String[] args;

    public JBOSApplicationEvent(JBOSApplication application, String[] args) {
        super(application);
        this.args = args;
    }

    public JBOSApplication getSpringApplication() {
        return (JBOSApplication) getSource();
    }

    public final String[] getArgs() {
        return this.args;
    }

}
