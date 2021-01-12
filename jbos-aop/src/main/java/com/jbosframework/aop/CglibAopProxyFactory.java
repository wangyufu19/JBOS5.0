package com.jbosframework.aop;

/**
 * CglibAopProxyFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public class CglibAopProxyFactory implements AopProxyFactory{
	/**
	 * 构造方法
	 */
	public CglibAopProxyFactory(){
		
	}
	/**
	 * 创建AOP代理
	 * @return
	 */
	@Override
	public AopProxy createAopProxy(AdviceConfig adviceConfig) {
		AopProxy aopProxy=new CglibProxy(adviceConfig);
		return aopProxy;
	}
}
