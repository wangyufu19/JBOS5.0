package com;

import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;

import com.jbosframework.boot.JBOSApplication;
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
		AspectBeanService aspectBean=ctx.getBean(AspectBeanService.class);
		aspectBean.aspect();
	}

}
