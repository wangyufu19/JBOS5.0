package com.application;

import com.application.sys.service.AsyncTaskService;
import com.application.sys.service.UserMgrService;
import com.application.sys.service.impl.UserMgrServiceImpl;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Future;

@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		UserMgrService userMgrService=ctx.getBean(UserMgrServiceImpl.class);
		log.info("userMgrService={}",userMgrService);

    }

}
