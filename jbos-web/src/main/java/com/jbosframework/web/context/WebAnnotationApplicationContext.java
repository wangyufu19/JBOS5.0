package com.jbosframework.web.context;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.support.AnnotationApplicationContext;
import com.jbosframework.web.mvc.annotation.WebAnnotationBeanRegistry;

/**
 * WebAnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class WebAnnotationApplicationContext {
	
	/**
	 * 构造方法
	 */
	public WebAnnotationApplicationContext(){
		
	}
	/**
	 * 初始化上下文
	 * @param cls
	 * @return
	 */
	public ApplicationContext initWebApplicationContext(Class<?> cls){
		ApplicationContext context=null; 
		context=new AnnotationApplicationContext();
		context.addBeanRegistry(new WebAnnotationBeanRegistry());
		context.registry(cls);
		return context;
	}
	/**
	 * 初始化上下文
	 * @param clses
	 * @return
	 */
	public ApplicationContext initWebApplicationContext(Class<?>[] clses){
		ApplicationContext context=null; 
		context=new AnnotationApplicationContext();
		context.addBeanRegistry(new WebAnnotationBeanRegistry());
		context.registry(clses);
		return context;
	}

}
