package com.jbosframework.aop;
import com.jbosframework.aop.AopProxyFactory;
import com.jbosframework.aop.CglibProxy;
/**
 * CglibAopProxyFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public class CglibAopProxyFactory implements AopProxyFactory{
	/**
	 * 代理实现类
	 */
	private Object proxyInstance;
	/**
	 * 代理接口
	 */
	private Class proxiedInterfaces;
	/**
	 * 构造方法
	 */
	public CglibAopProxyFactory(){
		
	}
	/**
	 * 构造方法
	 * @param proxyInstance
	 */
	public CglibAopProxyFactory(Object proxyInstance){
		this.proxyInstance=proxyInstance;
	}
	/**
	 * 得到代理实现类
	 * @return
	 */
	public Object getProxyInstance() {
		return proxyInstance;
	}
	/**
	 * 设置代理实现类
	 * @param proxyInstance
	 */
	public void setProxyInstance(Object proxyInstance) {
		this.proxyInstance = proxyInstance;
	}
	/**
	 * 得到代理接口
	 * @return
	 */
	public Class getProxiedInterfaces() {
		return proxiedInterfaces;
	}
	/**
	 * 设置代理接口
	 * @param proxiedInterfaces
	 */
	public void setProxiedInterfaces(Class proxiedInterfaces) {	
		this.proxiedInterfaces = proxiedInterfaces;
	}
	/**
	 * 创建AOP代理
	 * @return
	 */
	@Override
	public AopProxy createAopProxy() {
		AopProxy aopProxy=new CglibProxy(proxyInstance.getClass());
		return aopProxy;
	}
}
