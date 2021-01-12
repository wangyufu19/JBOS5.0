package com.jbosframework.aop;

/**
 * AopProxy
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public interface AopProxy {
	/**
	 * 得到代理类对象
	 * @return
	 */
	public <T> T getProxy();
}
