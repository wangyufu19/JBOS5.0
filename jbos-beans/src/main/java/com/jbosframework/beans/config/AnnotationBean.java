package com.jbosframework.beans.config;

import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.utils.StringUtils;

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


	public static AnnotationBean createAnnotationBean(String id,Class<?> cls){
		AnnotationBean annotationBean=new AnnotationBean();
		if(StringUtils.isNUll(id)) {
			id=cls.getName();
		}
		annotationBean.setId(id);
		annotationBean.setName(id);
		annotationBean.setClassName(cls.getName());
		Scope scope=cls.getAnnotation(Scope.class);
		if(scope!=null){
			annotationBean.setScope(scope.value());
		}
		return annotationBean;
	}
}