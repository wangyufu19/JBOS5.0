package com.jbosframework.aop;

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
	public AopProxy createAopProxy(AdviceConfig adviceConfig){
		AopProxy aopProxy=new JdkDynamicProxy(adviceConfig);
		return aopProxy;
	}
}
