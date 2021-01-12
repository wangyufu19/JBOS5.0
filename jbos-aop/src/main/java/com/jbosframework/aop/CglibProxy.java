package com.jbosframework.aop;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;  
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CglibProxy
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-10
 */
public class CglibProxy implements AopProxy{
	private static final Log log= LogFactory.getLog(CglibProxy.class);

	private AdviceConfig adviceConfig;
	/**
	 * 构造方法
	 * @param adviceConfig
	 */
	public CglibProxy(AdviceConfig adviceConfig){
		this.adviceConfig=adviceConfig;
	}
	/**
	 * 得到代理类对象
	 * @return
	 */
	public Object getProxy(){
		Object obj=null;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.adviceConfig.getTarget().getClass());
		enhancer.setCallback(new CglibProxy.CglibMethodInterceptor(
				this.adviceConfig.getTarget(),
				this.adviceConfig.getMethodBeforeAdvice(),
				this.adviceConfig.getMethodAfterAdvice()));
		obj=enhancer.create();
		return obj;
	}
	public class CglibMethodInterceptor implements MethodInterceptor{
		private Object target;
		private MethodBeforeAdvice methodBeforeAdvice;
		private MethodAfterAdvice methodAfterAdvice;

		public CglibMethodInterceptor(Object target,MethodBeforeAdvice methodBeforeAdvice,MethodAfterAdvice methodAfterAdvice){
			this.target=target;
			this.methodBeforeAdvice=methodBeforeAdvice;
			this.methodAfterAdvice=methodAfterAdvice;
		}
		@Override
		public Object intercept(Object object, Method method, Object[] arg,
								MethodProxy methodProxy) throws Throwable {
			//调用前
			if(this.methodBeforeAdvice!=null){
				this.methodBeforeAdvice.before(object,method,arg);
			}
			Object result = methodProxy.invoke(this.target,arg);
			//调用后
			if(this.methodAfterAdvice!=null){
				this.methodAfterAdvice.after(object,method,arg);
			}
			return result;
		}
	}

}
