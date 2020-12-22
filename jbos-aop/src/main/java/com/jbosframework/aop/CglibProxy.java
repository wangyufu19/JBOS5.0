package com.jbosframework.aop;
import java.lang.reflect.Method;

import com.jbosframework.aop.support.ProxyConfig;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;  
import net.sf.cglib.proxy.MethodProxy;
/**
 * CglibProxy
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-10
 */
public class CglibProxy implements AopProxy,MethodInterceptor{
	private ProxyConfig proxyConfig;

	/**
	 * 构造方法
	 * @param proxyConfig
	 */
	public CglibProxy(ProxyConfig proxyConfig){
		this.proxyConfig=proxyConfig;
	}
	/**
	 * 得到代理类对象
	 * @return
	 */
	public Object getProxy(){
		Object obj=null;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(proxyConfig.getTarget().getClass());
		enhancer.setCallback(this);
		obj=enhancer.create();
		return obj;
	}
	@Override
	public Object intercept(Object object, Method method, Object[] arg,
			MethodProxy methodProxy) throws Throwable {
		Object result = methodProxy.invokeSuper(object, arg);
		return result;  
	}
}
