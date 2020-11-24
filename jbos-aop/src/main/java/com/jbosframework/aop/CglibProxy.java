package com.jbosframework.aop;
import java.lang.reflect.Method;  
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
	private Class targetClass;
	/**
	 * 构造方法
	 * @param targetClass
	 */
	public CglibProxy(Class targetClass){
		this.targetClass=targetClass;
	}
	/**
	 * 创建代理类
	 * @return
	 */
	public Object createProxy(){
		Object obj=null;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
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
