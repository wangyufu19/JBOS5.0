package com.jbosframework.aop;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

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
				this.adviceConfig));
		obj=enhancer.create();
		return obj;
	}
	public class CglibMethodInterceptor implements MethodInterceptor{
		private AdviceConfig adviceConfig;

		public CglibMethodInterceptor(AdviceConfig adviceConfig){
			this.adviceConfig=adviceConfig;
		}
		@Override
		public Object intercept(Object object, Method method, Object[] args,
								MethodProxy methodProxy) throws Throwable {
			MethodCaller methodCaller=this.adviceConfig.getMethodCaller();
			AdviceMethodCaller caller=new AdviceMethodCaller(methodProxy,methodCaller);
			if(methodCaller.async()){
				return caller;
			}else{
				return caller.call();
			}
		}
	}
	public class AdviceMethodCaller implements Callable<Object> {
		private MethodProxy methodProxy;
		private MethodCaller methodCaller;
		private Object target;
		private Object object;
		private Method method;
		private Object[] args;

		public AdviceMethodCaller(MethodProxy methodProxy,MethodCaller methodCaller){
			this.methodProxy=methodProxy;
			this.methodCaller=methodCaller;
		}

		public Object getTarget() {
			return target;
		}

		public void setTarget(Object target) {
			this.target = target;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public Object[] getArgs() {
			return args;
		}

		public void setArgs(Object[] args) {
			this.args = args;
		}

		public Object call() throws Exception {
			//调用前
			methodCaller.before(object,method,args);
			Object result = null;
			try {
				result = methodProxy.invoke(this.getTarget(),this.getArgs());
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
			//调用后
			methodCaller.after(object,method,args);
			return result;
		}
	}
}
