package com.jbosframework.beans.config;
import com.jbosframework.beans.config.BeanDefinition;

/**
 * 
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBean extends BeanDefinition{
	
	private String[] requestMethod;
	
	
	public String[] getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String[] requestMethod) {
		this.requestMethod = requestMethod;
	}
}
