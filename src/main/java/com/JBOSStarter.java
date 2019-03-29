package com;
import com.application.examples.controller.UserMgrController;
import com.application.examples.service.A;
import com.application.examples.service.impl.AImpl;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.AopProxyFactory;
import com.jbosframework.beans.factory.BeanAopProxyFactory;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.support.AnnotationApplicationContext;
import java.io.IOException;
import java.lang.reflect.Proxy;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.JdkDynamicProxy;
import com.jbosframework.core.JBOSClassloader;
import com.jbosframework.boot.JBOSApplication;
@JBOSBootApplication
public class JBOSStarter {

	public static void main(String[] args) throws IOException {
		JBOSApplication jbosApplication=new JBOSApplication();
		ApplicationContext ctx=jbosApplication.start(JBOSStarter.class);
		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
		userMgrController.getUserList();
//		A a=ctx.getBean("a",AImpl.class);
//		a.sayHello();
	}

}
