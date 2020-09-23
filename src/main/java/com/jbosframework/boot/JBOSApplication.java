package com.jbosframework.boot;
import java.io.IOException;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.support.AnnotationApplicationContext;
import com.jbosframework.context.annotation.EnableAutoConfiguration;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.ComponentScan;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
/**
 * JBOSApplication
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSApplication {
    private ApplicationContext ctx=new AnnotationApplicationContext();
    public Class<?> jbosBootClass;

    /**
     * 构造方法
     * @param jbosBootClass
     */
    public JBOSApplication(Class<?> jbosBootClass){
        this.jbosBootClass=jbosBootClass;
    }
    /**
     * 初始化配置
     * @param jbosBootApplication
     */
    private void initConfiguration(Class<?> jbosBootApplication,String... args){
        //开启自动配置
        EnableAutoConfiguration enableAutoConfiguration=jbosBootApplication.getAnnotation(EnableAutoConfiguration.class);
        if(enableAutoConfiguration!=null) {
            ctx.getContextConfiguration().setEnableAutoConfiguration(true);
        }
        //开启切面自动代理
        EnableAspectJAutoProxy enableAspectJAutoProxy=jbosBootApplication.getAnnotation(EnableAspectJAutoProxy.class);
        if(enableAspectJAutoProxy!=null){
            ctx.getContextConfiguration().setEnableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
        }
    }
    /**
     * 启动应用
     * @param args
     * @return
     * @throws IOException
     */
    public ApplicationContext start(String... args) throws IOException {
        if(jbosBootClass==null){
            return ctx;
        }
        JBOSBootApplication jbosBootApplication=jbosBootClass.getAnnotation(JBOSBootApplication.class);
        if(jbosBootApplication!=null){
            this.initConfiguration(JBOSBootApplication.class,args);
        }else{
            if(jbosBootClass.getAnnotation(Configuration.class)==null||jbosBootClass.getAnnotation(ComponentScan.class)==null) {
                return ctx;
            }
            this.initConfiguration(jbosBootClass,args);
        }
        ctx.registry(jbosBootClass);
        return ctx;
    }
}
