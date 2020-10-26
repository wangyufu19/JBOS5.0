package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Import;
import com.jbosframework.core.JBOSClassloader;

/**
 * AutoConfigurationFactory
 * @author youfu.wang
 * @version 1.0
 */
public class AutoConfigurationContext {
    private ApplicationContext applicationContext;

    /**
     * 构造方法
     * @param applicationContext
     */
    public AutoConfigurationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    /**
     * 加载自动配置
     * @param jbosBootClass
     */
    public void load(Class<?> jbosBootClass){
        JBOSBootApplication jbosBootApplication=jbosBootClass.getAnnotation(JBOSBootApplication.class);
        if(jbosBootApplication==null){
            return;
        }
        //开启自动配置
        EnableAutoConfiguration enableAutoConfiguration=jbosBootApplication.getClass().getAnnotation(EnableAutoConfiguration.class);
        Class<?>[] importClasses=null;
        if(enableAutoConfiguration!=null) {
            Import importAnnotation=enableAutoConfiguration.getClass().getAnnotation(Import.class);
            if(importAnnotation!=null){
                importClasses=importAnnotation.value();
            }
            if(importClasses!=null){
                for(Class cls:importClasses){
                    Object obj=JBOSClassloader.newInstance(cls);
                    if(obj instanceof AutoConfigurationRegister){
                        AutoConfigurationRegister register=(AutoConfigurationRegister)obj;
                        register.registry(this.applicationContext);
                    }
                }
            }
        }
        //开启切面自动代理
        EnableAspectJAutoProxy enableAspectJAutoProxy=jbosBootApplication.getClass().getAnnotation(EnableAspectJAutoProxy.class);
        if(enableAspectJAutoProxy!=null){
            applicationContext.getContextConfiguration().setEnableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
        }
    }
}
