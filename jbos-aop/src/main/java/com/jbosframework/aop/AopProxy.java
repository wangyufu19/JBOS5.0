package com.jbosframework.aop;

import com.jbosframework.aop.support.ProxyConfig;

/**
 * AopProxy
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public interface AopProxy {
	/**
	 * 创建代理类对象
	 * @return
	 */
	public <T> T createProxy();
}
