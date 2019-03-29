package com.jbosframework.beans.annotation;
import java.lang.annotation.Annotation;
/**
 * AnnotationFilter
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationFilter {
	
	private Class<?>[] include;
	
	/**
	 * 设置包含的注解
	 * @param include
	 */
	public void setInclude(Class<?>[] include) {
		this.include=include;
	}
	/**
	 * 判断是否包含的注解
	 * @param annotation
	 * @return
	 */
	public boolean filter(Annotation annotation) {
		boolean bool=false;
		if(include==null) {
			return true;
		}else if(include.length<=0) {
			return true;
		}
		if(annotation==null) {
			return false;
		}
		for(int i=0;i<include.length;i++) {
			if(include[i].getName().equals(annotation.annotationType().getName())) {
				bool=true;
				break;
			}
		}
		return bool;
	}
}
