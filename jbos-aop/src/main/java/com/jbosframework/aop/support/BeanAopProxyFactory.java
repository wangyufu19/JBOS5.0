package com.jbosframework.aop.support;
import com.jbosframework.aop.AopProxyFactory;
import com.jbosframework.aop.CglibAopProxyFactory;
import com.jbosframework.aop.JdkAopProxyFactory;

/**
 * BeanAopProxyFactory
 * @author youfu.wang
 * @version 1.0
 */
public class BeanAopProxyFactory {
	private static AopProxyFactory aopProxyFactory=null;
	/**
	 * 创建AopProxyFactory实例
	 * @return
	 */
	public static AopProxyFactory createJdkAopProxyFactory(){
		if(aopProxyFactory==null){
			synchronized(AopProxyFactory.class){
				if(aopProxyFactory==null){
					aopProxyFactory=new JdkAopProxyFactory();
				}
			}
		}
		return aopProxyFactory;
	}
	/**
	 * 创建AopProxyFactory实例
	 * @return
	 */
	public static AopProxyFactory createCglibAopProxyFactory(){
		if(aopProxyFactory==null){
			synchronized(AopProxyFactory.class){
				if(aopProxyFactory==null){
					aopProxyFactory=new CglibAopProxyFactory();
				}
			}
		}
		return aopProxyFactory;
	}
}
