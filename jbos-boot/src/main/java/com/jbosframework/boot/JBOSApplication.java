package com.jbosframework.boot;

import com.jbosframework.boot.autoconfig.AutoConfigurationContext;
import com.jbosframework.boot.autoconfig.EnableAspectJAutoProxy;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.boot.autoconfig.transaction.TransactionBeanProcessor;
import com.jbosframework.boot.context.ConfigurationPropertiesChecker;
import com.jbosframework.boot.web.JBOSWebApplicationContext;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.configuration.Configuration;
import com.jbosframework.context.support.AnnotationApplicationContext;
import com.jbosframework.context.support.AspectProxyContext;
import com.jbosframework.transaction.annotation.EnableTransactionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JBOSApplication
 * @author youfu.wang
 * @version 5.0
 */
public class JBOSApplication {
    public static final Log logger= LogFactory.getLog(JBOSApplication.class);
    private ApplicationContext ctx;
    public Class<?> jbosBootClass;

    /**
     * 构造方法
     * @param jbosBootClass
     */
    public JBOSApplication(Class<?> jbosBootClass){
        this.jbosBootClass=jbosBootClass;
    }
    /**
     * 初始化上下文
     * @param args
     */
    private void prepareContext(String... args){
        //初始化上下文配置
        Configuration configuration=new Configuration();
        ctx=new AnnotationApplicationContext(configuration);
        JBOSBootApplication jbosBootApplication=jbosBootClass.getAnnotation(JBOSBootApplication.class);
        if(jbosBootApplication!=null){
            //开启切面自动代理
            EnableAspectJAutoProxy enableAspectJAutoProxy= JBOSBootApplication.class.getAnnotation(EnableAspectJAutoProxy.class);
            if(enableAspectJAutoProxy!=null){
                AspectProxyContext aspectProxyContext=new AspectProxyContext(ctx);
                aspectProxyContext.enableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
            }
            //开启事务管理
            EnableTransactionManager enableTransactionManage=JBOSBootApplication.class.getAnnotation(EnableTransactionManager.class);
            if(enableTransactionManage!=null){
                TransactionBeanProcessor transactionBeanProcessor=new TransactionBeanProcessor(ctx);
                transactionBeanProcessor.setOrder(15);
                ctx.addBeanPostProcessor(transactionBeanProcessor);
            }
        }

        ConfigurationPropertiesChecker configurationPropertiesChecker=new ConfigurationPropertiesChecker();
        configurationPropertiesChecker.setApplicationContext(ctx);
        ctx.addBeanBeforeProcessor(configurationPropertiesChecker);
    }

    /**
     * 完成上下文
     */
    public void finishContext(){
        //初始化和启动Web容器
        JBOSWebApplicationContext jbosWebApplicationContext=new JBOSWebApplicationContext(ctx,jbosBootClass);
        jbosWebApplicationContext.onStartup();
        //加载自动配置的组件类到容器中
        AutoConfigurationContext autoConfigurationContext=new AutoConfigurationContext(ctx);
        autoConfigurationContext.load(jbosBootClass);
        //刷新容器上下文
        ctx.refreshContext();
    }
    /**
     * 启动应用
     * @param args
     * @return
     */
    public ApplicationContext start(String... args)  {
        long stime=System.currentTimeMillis();
        long etime=System.currentTimeMillis();
        //初始化容器上下文
        this.prepareContext(args);
        //完成容器上下文
        this.finishContext();
        etime=System.currentTimeMillis();
        logger.info("Started "+JBOSApplication.class.getSimpleName()+" in "+(etime-stime)/1000+" seconds");
        return ctx;
    }
}
