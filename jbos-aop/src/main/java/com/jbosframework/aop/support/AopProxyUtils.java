package com.jbosframework.aop.support;

import com.jbosframework.aop.Advised;
import com.jbosframework.aop.SingletonTargetSource;
import com.jbosframework.aop.TargetSource;

/**
 * AopProxyUtils
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public class AopProxyUtils {

	public static Object getSingletonTarget(Object candidate) {
		if (candidate instanceof Advised) {
			TargetSource targetSource = ((Advised)candidate).getTargetSource();
			if (targetSource instanceof SingletonTargetSource) {
				return ((SingletonTargetSource)targetSource).getTarget();
			}
		}

		return null;
	}
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
