package com.jbosframework.boot.context.event;

import com.jbosframework.boot.JBOSApplication;

public class ApplicationStartingEvent extends JBOSApplicationEvent {

    public ApplicationStartingEvent(JBOSApplication application, String[] args) {
        super(application, args);
    }


}
