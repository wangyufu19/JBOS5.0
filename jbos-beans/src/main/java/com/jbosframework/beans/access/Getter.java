package com.jbosframework.beans.access;
/**
 * Getter接口对象
 * @author youfu.wang
 * @version 1.0
 */
public interface Getter {
	public Object get(Object obj);
	public Class getReturnType();
}
