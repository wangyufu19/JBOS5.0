package com.jbosframework.web.context;
import com.jbosframework.context.ApplicationContext;

/**
 * WebAnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class WebAnnotationApplicationContext {
	private ApplicationContext applicationContext=null;

	public WebAnnotationApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
	}

	public void initWebApplicationContext(Class<?> cls){

	}

	public void initWebApplicationContext(Class<?>[] clses){
	}

}
