package com.jbosframework.aop;

import com.jbosframework.aop.support.ProxyConfig;

/**
 * JdkAopProxyFactory
 * @author youfu.wang
 * @version 5.0
 */
public class JdkAopProxyFactory implements AopProxyFactory{

	/**
	 * 构造方法
	 */
	public JdkAopProxyFactory(){
		
	}
	/**
	 * 创建AOP代理
	 * @return
	 */
	@Override
	public AopProxy createAopProxy(ProxyConfig proxyConfig){
		AopProxy aopProxy=new JdkDynamicProxy(proxyConfig);
		return aopProxy;
	}
}
