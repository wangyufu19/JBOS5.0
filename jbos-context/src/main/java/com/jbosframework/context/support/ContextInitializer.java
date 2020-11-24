package com.jbosframework.context.support;
import com.jbosframework.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.context.configuration.Configuration;
/**
 * ContextInitializer
 * @author youfu.wang
 * @version 1.0
 */
public class ContextInitializer {
    private Configuration configuration=new Configuration();
    private AspectProxyBeanContext aspectProxyBeanContext=new AspectProxyBeanContext();
    /**
     * 构造方法
     */
    public ContextInitializer() {
        configuration.load();
    }
    public Configuration getContextConfiguration(){
        return configuration;
    }
    public AspectProxyBeanContext getAspectProxyBeanContext(){
        return aspectProxyBeanContext;
    }
}
