package com.jbosframework.context.support;
import com.jbosframework.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.context.configuration.Configuration;
/**
 * ContextInitializer
 * @author youfu.wang
 * @version 1.0
 */
public class ContextInitializer {

    private AspectProxyBeanContext aspectProxyBeanContext=new AspectProxyBeanContext();

    private Configuration configuration;

    private boolean enableAspectJAutoProxy=false;

    public ContextInitializer(){

    }
    /**
     * 构造方法
     * @param configuration
     */
    public ContextInitializer(Configuration configuration){
        this.configuration=configuration;
    }

    /**
     * 得到上下文配置
     * @return
     */
    public Configuration getContextConfiguration(){
        return configuration;
    }
    /**
     * 设置启用切面
     * @param enableAspectJAutoProxy
     */
    public void setEnableAspectJAutoProxy(boolean enableAspectJAutoProxy){
        this.enableAspectJAutoProxy=enableAspectJAutoProxy;
    }

    /**
     * 是否启用切面
     * @return
     */
    public boolean isEnableAspectJAutoProxy(){
        return this.enableAspectJAutoProxy;
    }


    public AspectProxyBeanContext getAspectProxyBeanContext(){
        return aspectProxyBeanContext;
    }

}
