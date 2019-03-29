package com.jbosframework.boot;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.AutoConfiguration;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;
import com.jbosframework.context.support.AnnotationApplicationContext;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import java.io.IOException;
import com.jbosframework.context.configuration.Configuration;
/**
 * JBOSApplication
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSApplication {
    private ApplicationContext ctx=new AnnotationApplicationContext();

    public ApplicationContext start(Class<?> cls) throws IOException {
        if(cls==null){
            return ctx;
        }
        JBOSBootApplication jbosBootApplication=cls.getAnnotation(JBOSBootApplication.class);
        if(jbosBootApplication!=null){
            Configuration configuration=new Configuration();
            //切面自动代理
            EnableAspectJAutoProxy enableAspectJAutoProxy=JBOSBootApplication.class.getAnnotation(EnableAspectJAutoProxy.class);
            if(enableAspectJAutoProxy!=null){
                configuration.setEnableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
            }
            //自动扫描配置
            AutoConfiguration autoConfiguration=JBOSBootApplication.class.getAnnotation(AutoConfiguration.class);
            if(autoConfiguration!=null){
                System.out.println("******packages: "+cls.getPackage().getName());
                ctx.setContextConfiguration(configuration);
                ctx.scan(cls.getPackage().getName());
            }
        }else{
            ctx.register(JBOSBootApplication.class);
        }
        return ctx;
    }
}
