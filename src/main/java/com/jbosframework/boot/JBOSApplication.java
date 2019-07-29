package com.jbosframework.boot;
import java.io.IOException;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.support.AnnotationApplicationContext;
import com.jbosframework.context.annotation.EnableAutoConfiguration;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;
import com.jbosframework.context.annotation.ComponentScan;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.configuration.Configuration;
/**
 * JBOSApplication
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSApplication {
    private ApplicationContext ctx=new AnnotationApplicationContext();

    /**
     * 初始化配置
     * @param cls
     */
    private void initConfiguration(Class<?> cls){
        //开启自动配置
        EnableAutoConfiguration enableAutoConfiguration=cls.getAnnotation(EnableAutoConfiguration.class);
        if(enableAutoConfiguration!=null) {
            ctx.getContextConfiguration().setEnableAutoConfiguration(true);
        }
        //开启切面自动代理
        EnableAspectJAutoProxy enableAspectJAutoProxy=cls.getAnnotation(EnableAspectJAutoProxy.class);
        if(enableAspectJAutoProxy!=null){
            ctx.getContextConfiguration().setEnableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
        }
    }
    public ApplicationContext start(Class<?> cls) throws IOException {
        if(cls==null){
            return ctx;
        }
        JBOSBootApplication jbosBootApplication=cls.getAnnotation(JBOSBootApplication.class);
        if(jbosBootApplication!=null){
            if(JBOSBootApplication.class.getAnnotation(com.jbosframework.context.annotation.Configuration.class)==null||JBOSBootApplication.class.getAnnotation(ComponentScan.class)==null) {
                return ctx;
            }
            this.initConfiguration(JBOSBootApplication.class);
        }else{
            if(cls.getAnnotation(com.jbosframework.context.annotation.Configuration.class)==null||cls.getAnnotation(ComponentScan.class)==null) {
                return ctx;
            }
            this.initConfiguration(cls);
        }
        ctx.registry(cls);
        return ctx;
    }
}
