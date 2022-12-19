package com.jbosframework.context.event;

import com.jbosframework.aop.support.AopProxyUtils;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.context.ApplicationEvent;
import com.jbosframework.context.ApplicationListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class AbstractApplicationEventMulticaster {
    private static final Log logger= LogFactory.getLog(AbstractApplicationEventMulticaster.class);
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
        Set<ApplicationListener<?>> beanListeners = new LinkedHashSet();
        if(this.beanFactory!=null){
            String[] applicationListeners=this.beanFactory.getBeanNamesOfType(ApplicationListener.class);
            for(String applicationListener:applicationListeners){
                ApplicationListener  beanListener=(ApplicationListener)this.beanFactory.getBean(applicationListener);
                try {
                    Method method=beanListener.getClass().getDeclaredMethod("onApplicationEvent",event.getClass());
                    int mod = method.getModifiers();
                    if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                        beanListeners.add(beanListener);
                    }
                } catch (NoSuchMethodException e) {
                    if(logger.isDebugEnabled()){
                        logger.debug(e);
                    }
                }
            }
        }
        if(beanListeners.size()>0){
            return beanListeners;
        }else{
            return applicationListeners;
        }
    }
}
