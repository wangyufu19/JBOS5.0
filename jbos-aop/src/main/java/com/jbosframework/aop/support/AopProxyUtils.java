package com.jbosframework.aop.support;
/**
 * AopProxyUtils
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public class AopProxyUtils {
	/**
	 * 判断是否AOP代理
	 * @param obj
	 * @return
	 */
	public static boolean isAopProxyBean(Object obj){
		if(obj!=null){
			if(obj instanceof ProxyFactoryBean){
				return true;
			}
		}
		return false;
	}
	/**
	 * 得到AOP代理实例
	 * @param obj
	 * @return
	 */
	public static Object getAopProxy(Object obj) throws Exception {
		ProxyFactoryBean proxyFactoryBean=(ProxyFactoryBean)obj;
		return proxyFactoryBean.getObject();
	}
}
