package com.jbosframework.beans.access;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Setter实现类
 * @author youfu.wang
 * @version 1.0
 */
public class BaseSetter implements Setter {
	private Class cls;
	private String propertyName;
	private String methodName;

	public BaseSetter(Class cls, String propertyName) {
		this.cls = cls;
		this.propertyName = propertyName;
	}
	public void set(Object obj, Object value) {
		if (value == null)
			return;
		methodName = this.getMethodName();
		Method method = null;
		try {
			method = cls.getMethod(methodName,value.getClass());			
			method.invoke(obj,value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			try {
				method = cls.getMethod(methodName, value.getClass().getInterfaces());
				method.invoke(obj, value);
			} catch (SecurityException e1) {				
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {			
				try {
					method = cls.getMethod(methodName, value.getClass().getSuperclass());
					method.invoke(obj, value);
				} catch (SecurityException e2) {
					e2.printStackTrace();
				} catch (NoSuchMethodException e2) {
					e2.printStackTrace();
				} catch (IllegalArgumentException e2) {
					e2.printStackTrace();
				} catch (IllegalAccessException e2) {
					e2.printStackTrace();
				} catch (InvocationTargetException e2) {
					e2.printStackTrace();
				}
				
			} catch (IllegalArgumentException e1) {			
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {				
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {				
				e1.printStackTrace();
			}			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getMethodName() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < propertyName.length(); i++) {
			if (i == 0) {
				buf.append(Character.toUpperCase(propertyName.charAt(i)));
			} else
				buf.append(propertyName.charAt(i));
		}
		methodName = "set" + buf.toString();
		return this.methodName;
	}
}
