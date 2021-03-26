package com.jbosframework.context.support;

import com.jbosframework.beans.factory.BeanFactory;
/**
 * BeanProcessorRegistry
 * @author youfu.wang
 * @version 5.0
 */
public abstract class BeanProcessorRegistry {
    private BeanFactory beanFactory;

    /**
     * 构造方法
     */
    public BeanProcessorRegistry(){

    }
    /**
     * 构造方法
     * @param beanFactory
     */
    public BeanProcessorRegistry(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public BeanFactory getBeanFactory(){
        return beanFactory;
    }

    public abstract  void registryBeanProcessor();
}
