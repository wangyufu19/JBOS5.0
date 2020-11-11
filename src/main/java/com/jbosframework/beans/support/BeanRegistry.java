package com.jbosframework.beans.support;

import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.ApplicationContext;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public abstract class BeanRegistry {
    private ApplicationContext ctx;

    /**
     * 构造方法
     */
    public BeanRegistry(){

    }
    /**
     * 构造方法
     * @param ctx
     */
    public BeanRegistry(ApplicationContext ctx){
        this.ctx=ctx;
    }
    public void setApplicationContext(ApplicationContext ctx){
        this.ctx=ctx;
    }
    public ApplicationContext getApplicationContext(){
        return ctx;
    }
    //注册Bean的接口
    public void registryBeanInterfaces(Class<?> cls, AnnotationBean annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.getApplicationContext().putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    public abstract void registerBean(Class<?> cls);
}
