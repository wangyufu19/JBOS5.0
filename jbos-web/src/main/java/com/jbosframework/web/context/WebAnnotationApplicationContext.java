package com.jbosframework.web.context;
import com.jbosframework.context.ApplicationContext;

/**
 * WebAnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class WebAnnotationApplicationContext {
	private ApplicationContext applicationContext=null;
	/**
	 * 构造方法
	 */
	public WebAnnotationApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
		//applicationContext.addBeanRegistry(new WebAnnotationBeanRegistry(applicationContext));
	}
	/**
	 * 初始化上下文
	 * @param cls
	 * @return
	 */
	public void initWebApplicationContext(Class<?> cls){
		//applicationContext.registry(cls);
	}
	/**
	 * 初始化上下文
	 * @param clses
	 * @return
	 */
	public void initWebApplicationContext(Class<?>[] clses){
		//applicationContext.registry(clses);
	}

}
