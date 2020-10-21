package com;

import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import java.io.IOException;

import com.jbosframework.boot.JBOSApplication;
import com.application.examples.controller.UserMgrController;
import lombok.extern.slf4j.Slf4j;

@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) throws IOException {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
//		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
//		userMgrController.getUserList();
	}

}
