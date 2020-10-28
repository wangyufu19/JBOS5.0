package com.jbosframework.boot;

import com.jbosframework.boot.autoconfig.AutoConfigurationContext;
import com.jbosframework.boot.web.JBOSWebApplicationContext;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.support.AnnotationApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JBOSApplication
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSApplication {
    public static final Log logger= LogFactory.getLog(JBOSApplication.class);
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
     * @param args
     */
    private void initConfiguration(String... args){

    }
    /**
     * 启动应用
     * @param args
     * @return
     */
    public ApplicationContext start(String... args)  {
        long stime=System.currentTimeMillis();
        long etime=System.currentTimeMillis();
        //初始化环境配置
        this.initConfiguration(args);
        //注册和扫描启动类所在包和子包下的所有组件类到容器中
        ctx.registry(jbosBootClass);
        //加载自动配置的组件类到容器中
        AutoConfigurationContext autoConfigurationContext=new AutoConfigurationContext(ctx);
        autoConfigurationContext.load(jbosBootClass);
        //刷新容器上下文
        ctx.refreshContext();
        //初始化和启动Web容器
        JBOSWebApplicationContext jbosWebApplicationContext=new JBOSWebApplicationContext(ctx);
        jbosWebApplicationContext.onStartup();
        etime=System.currentTimeMillis();
        logger.info("Started "+JBOSApplication.class.getSimpleName()+" in "+(etime-stime)/1000+" seconds");
        return ctx;
    }
}
