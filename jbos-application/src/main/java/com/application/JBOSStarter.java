package com.application;

import com.application.sys.pojo.UserInfo;
import com.application.sys.service.UserMgrService;
import com.application.sys.service.impl.UserMgrServiceImpl;
import com.application.test.aspect.AspectInvoker;
import com.application.test.async.AsyncTaskService;
import com.application.test.listener.NotifyEvent;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.core.io.support.JBOSFactoriesLoader;
import com.jbosframework.schedule.annotation.async.EnableAsync;
import com.jbosframework.schedule.concurrent.ThreadPoolTaskExecutor;
import com.jbosframework.utils.JBOSClassloader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;


@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException, SQLException, IOException {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		ctx.publishEvent(new NotifyEvent(new Object()));
//		UserMgrService userMgrService=ctx.getBean(UserMgrService.class);
//		List<UserInfo> userInfos=userMgrService.getUserList(null);
//		log.info("userInfos={}",userInfos);
//		AsyncTaskService asyncTaskService=ctx.getBean(AsyncTaskService.class);
//		asyncTaskService.doTask1();
//		asyncTaskService.doTask2();
//		asyncTaskService.doTask3();
//        AdviceConfig adviceConfig=new AdviceConfig();
//        adviceConfig.setProxyInterfaces(new Class[]{UserMgrService.class});
//        JdkDynamicProxy aopProxy=new JdkDynamicProxy(adviceConfig);
//        UserMgrService userMgrService=(UserMgrService)aopProxy.getProxy();
//        userMgrService.getUserInfoByLoginName("");
//		AspectInvoker aspectInvoker=ctx.getBean(AspectInvoker.class);
//		aspectInvoker.invoke();
	}

	@Setter
	@Getter
	public static class ResponseResult<T>{
		private int code;
		private String message;
		private T data;

	}
}

