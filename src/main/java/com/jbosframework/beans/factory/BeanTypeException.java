package com.jbosframework.beans.factory;
import java.lang.Throwable;
/**
 * BeanTypeException
 * @author youfu.wang
 * @version 1.0
 */
public class BeanTypeException extends Throwable{
	/**
	 * 构造方法
	 */
	public BeanTypeException(){
		super();
	}
	/**
	 * 构造方法
	 * @param message
	 */
	public BeanTypeException(String message){
		super(message);
	}
	/**
	 * 构造方法
	 * @param message
	 * @param cause
	 */
	public BeanTypeException(String message,Throwable cause){
		super(message,cause);
	}
	/**
	 * 构造方法
	 * @param cause
	 */
	public BeanTypeException(Throwable cause){
		super(cause);
	}
}
