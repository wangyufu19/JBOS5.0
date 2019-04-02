package com;
import com.application.examples.controller.UserMgrController;
import com.application.examples.pojo.User;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import java.io.IOException;
import com.jbosframework.boot.JBOSApplication;

@JBOSBootApplication
public class JBOSStarter {

	public static void main(String[] args) throws IOException {
		JBOSApplication jbosApplication=new JBOSApplication();
		ApplicationContext ctx=jbosApplication.start(JBOSStarter.class);
		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
		userMgrController.getUserList();
	}

}
