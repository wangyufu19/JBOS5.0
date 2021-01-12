package com.jbosframework.aop;

/**
 * AopProxyFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public interface AopProxyFactory {
	/**
	 * 创建AOP代理
	 * @return
	 */
	public AopProxy createAopProxy(AdviceConfig adviceConfig);
}
