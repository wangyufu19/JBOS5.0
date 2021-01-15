package com.application;

import com.jbosframework.boot.autoconfig.EnableAspectJAutoProxy;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.application.test.aspect.AspectInvoker;
import lombok.extern.slf4j.Slf4j;


@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
//		ProxyFactoryBean proxyFactoryBean=new ProxyFactoryBean();
//		proxyFactoryBean.setTarget(new UserAuthServiceImpl());
//		proxyFactoryBean.setProxyInterfaces(new Class[]{UserAuthService.class});
//		UserAuthService obj=(UserAuthService)proxyFactoryBean.getObject();
//		System.out.println(obj.toString());
		AspectInvoker aspectInvoker=ctx.getBean(AspectInvoker.class);
		aspectInvoker.invoke();
	}

}
