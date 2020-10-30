package com;

import com.application.examples.service.UserMgrService;
import com.application.examples.service.impl.UserMgrServiceImpl;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import java.io.IOException;

import com.jbosframework.boot.JBOSApplication;
import com.application.examples.controller.UserMgrController;
import com.jbosframework.test.AspectBean;
import com.jbosframework.test.AspectBeanService;
import lombok.extern.slf4j.Slf4j;

@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
//		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
//		userMgrController.getUserList();
		AspectBean aspectBean=ctx.getBean(AspectBean.class);
		aspectBean.getAspect();
	}

}
