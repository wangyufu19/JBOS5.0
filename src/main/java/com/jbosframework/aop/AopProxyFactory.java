package com.jbosframework.aop;
import com.jbosframework.aop.AopProxy;
/**
 * AopProxyFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public interface AopProxyFactory {

	/**
	 * 得到代理实现类
	 * @return
	 */
	public Object getProxyInstance();
	/**
	 * 设置代理实现类
	 * @param proxyInstance
	 */
	public void setProxyInstance(Object proxyInstance);
	/**
	 * 得到代理接口
	 * @return
	 */
	public Class getProxiedInterfaces();
	/**
	 * 设置代理接口
	 * @param proxiedInterfaces
	 */
	public void setProxiedInterfaces(Class proxiedInterfaces);
	/**
	 * 创建AOP代理
	 * @return
	 */
	public AopProxy createAopProxy();
}
