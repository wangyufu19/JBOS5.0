package com.jbosframework.beans.access;

/**
 * POJO读取工厂类
 * @author youfu.wang
 * @version 1.0
 */
public class BeanFactory {
	public static Getter getGetter(Class cls,String propertyName){
		Getter getter=new BaseGetter(cls,propertyName);		
		return getter;
	}
	public static Setter getSetter(Class cls,String propertyName){
		Setter setter=new BaseSetter(cls,propertyName);		
		return setter;
	}

}
