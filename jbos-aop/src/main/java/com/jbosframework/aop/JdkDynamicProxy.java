package com.jbosframework.aop;
import java.lang.reflect.*;

import com.jbosframework.aop.support.ProxyConfig;
import com.jbosframework.utils.JBOSClassloader;

/**
 * JdkDynamicProxy
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-10
 */
public class JdkDynamicProxy implements AopProxy,InvocationHandler{
	private ProxyConfig proxyConfig;

	/**
	 * 构造方法
	 * @param proxyConfig
	 */
	public JdkDynamicProxy(ProxyConfig proxyConfig){
		this.proxyConfig=proxyConfig;
	}
	/**
	 * 得到代理类对象
	 * @return
	 */
	public Object getProxy(){
		Object obj=null;
		obj=Proxy.newProxyInstance(JBOSClassloader.getClassLoader(), proxyConfig.getProxyInterfaces(), this);
		return obj;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result=null;
		if(args==null){
			result=call(this.proxyConfig.getTarget(), method);
		}else{
			result=call(this.proxyConfig.getTarget(), method,args);
		}
		return result;
	}
	private boolean isDefaultMethod(Method method) {
		return (method.getModifiers()
				& (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
				&& method.getDeclaringClass().isInterface();
	}
	private Object call(Object proxy, Method method,Object[] args){
		Object result=null;
		if(proxy!=null){
			try {
				result=method.invoke(proxy,args);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}		
		}	
		return result;
	}
	private Object call(Object proxy, Method method){
		Object result=null;	
		try {
			if(proxy!=null){
				result=method.invoke(proxy);
			}			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
}
