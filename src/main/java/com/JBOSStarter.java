package com;

import com.application.examples.service.UserMgrService;
import com.application.examples.service.impl.UserMgrServiceImpl;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import java.io.IOException;
import java.util.Map;

import com.jbosframework.boot.JBOSApplication;
import com.application.examples.controller.UserMgrController;

@JBOSBootApplication
public class JBOSStarter {

	public static void main(String[] args) throws IOException {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
		userMgrController.getUserList();
	}

}
