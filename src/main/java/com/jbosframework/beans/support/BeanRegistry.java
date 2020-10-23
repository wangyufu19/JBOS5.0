package com.jbosframework.beans.support;

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
    /**
     * 注册Bean
     * @param cls
     */
    public abstract void registerBean(Class<?> cls);
}
