package com.application;

import com.application.sys.service.UserAuthService;
import com.application.sys.service.impl.UserAuthServiceImpl;
import com.application.sys.service.impl.UserTokenServiceImpl;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.aop.support.ProxyConfig;
import com.jbosframework.aop.support.ProxyFactoryBean;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanAutowiredProcessor;
import com.jbosframework.boot.autoconfig.EnableAspectJAutoProxy;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.jbosframework.context.support.AnnotationAspectjProcessor;
import com.jbosframework.context.support.BeanFactoryContext;
import com.jbosframework.orm.mybatis.support.AnnotationMapperProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@JBOSBootApplication
@EnableAspectJAutoProxy
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		ProxyFactoryBean proxyFactoryBean=new ProxyFactoryBean();
		proxyFactoryBean.setTarget(new UserAuthServiceImpl());
		proxyFactoryBean.setProxyInterfaces(new Class[]{UserAuthService.class});
		UserAuthService obj=(UserAuthService)proxyFactoryBean.getObject();
		System.out.println(obj.toString());
	}

}
