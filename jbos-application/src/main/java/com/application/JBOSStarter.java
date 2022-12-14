package com.application;

import com.application.sys.service.AsyncTaskService;
import com.application.sys.service.UserMgrService;
import com.application.sys.service.impl.UserMgrServiceImpl;
import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.JdkDynamicProxy;
import com.jbosframework.boot.autoconfig.AutoConfigurationRegistry;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AnnotationConfigApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;
import com.jbosframework.context.support.AbstractApplicationContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.concurrent.*;


@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		DataSource dataSource=ctx.getBean(DataSource.class);
//		UserMgrService userMgrService=ctx.getBean(UserMgrServiceImpl.class);
		log.info("dataSource={}",dataSource);
//        AdviceConfig adviceConfig=new AdviceConfig();
//        adviceConfig.setProxyInterfaces(new Class[]{UserMgrService.class});
//        JdkDynamicProxy aopProxy=new JdkDynamicProxy(adviceConfig);
//        UserMgrService userMgrService=(UserMgrService)aopProxy.getProxy();
//        userMgrService.getUserInfoByLoginName("");

    }


}

