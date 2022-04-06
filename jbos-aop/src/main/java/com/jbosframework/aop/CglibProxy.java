package com.jbosframework.aop;
import net.sf.cglib.proxy.Enhancer;
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
		enhancer.setCallback(this.adviceConfig.getMethodAdvisor());
		obj=enhancer.create();
		return obj;
	}
}
