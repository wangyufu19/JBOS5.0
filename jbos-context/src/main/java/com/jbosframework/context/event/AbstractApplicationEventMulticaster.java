package com.jbosframework.context.event;

import com.jbosframework.aop.support.AopProxyUtils;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;

import java.util.*;

public abstract class AbstractApplicationEventMulticaster {
    private Object eventMutex=new Object();
    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet();
    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public void addApplicationListener(ApplicationListener<?> listener) {
        synchronized(this.eventMutex) {
            Object singletonTarget = AopProxyUtils.getSingletonTarget(listener);
            if (singletonTarget instanceof ApplicationListener) {
                this.applicationListeners.remove(singletonTarget);
            }

            this.applicationListeners.add(listener);
        }
    }
    protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event) {
        return applicationListeners;
    }
}
