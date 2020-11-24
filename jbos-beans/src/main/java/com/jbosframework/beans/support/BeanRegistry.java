package com.jbosframework.beans.support;

import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanFactory;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public abstract class BeanRegistry {
    private BeanFactory beanFactory;

    /**
     * 构造方法
     */
    public BeanRegistry(){

    }
    /**
     * 构造方法
     * @param beanFactory
     */
    public BeanRegistry(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public BeanFactory getBeanFactory(){
        return beanFactory;
    }
    //注册Bean的接口
    public void registryBeanInterfaces(Class<?> cls, AnnotationBean annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.getBeanFactory().putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    public abstract void registerBean(Class<?> cls);
}
