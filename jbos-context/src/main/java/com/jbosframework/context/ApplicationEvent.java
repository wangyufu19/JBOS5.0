package com.jbosframework.context;

import java.util.EventObject;

/**
 * ApplicationEvent
 * @author youfu.wang
 * @version 1.0
 */
public abstract class ApplicationEvent extends EventObject {
    private final long timestamp=System.currentTimeMillis();


    public ApplicationEvent(Object source) {
        super(source);
    }

    public final long getTimestamp(){
        return this.timestamp;
    }
}
