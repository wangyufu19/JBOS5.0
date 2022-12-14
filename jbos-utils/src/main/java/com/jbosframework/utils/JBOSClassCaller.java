package com.jbosframework.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * JBOSClassCaller
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSClassCaller {
	private static final Log log= LogFactory.getLog(JBOSClassCaller.class);

	public static Object call(Object obj,String method){
		Object result=null;
		try {
			if(obj!=null){
				result=call(obj,obj.getClass().getMethod(method));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Object obj,Method method){
		Object result=null;
		try {
			if(obj!=null){
				result=method.invoke(obj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}  catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Class<?> cls,String method){
		Object result=null;
		Object obj=null;
		Method methodObj;
		try {
			methodObj = cls.getMethod(method);
			obj=cls.newInstance();
			if(obj!=null){
				result=methodObj.invoke(obj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object call(String cls,String method){
		Object result=null;		
		Object obj=null;
		try {
			Class<?> clazz=JBOSClassloader.loadClass(cls);
			obj=call(clazz,method);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object call(String cls,String method,Object[] args){
		Object result=null;
		Object obj=null;
		try {
			Class<?> clazz=JBOSClassloader.loadClass(cls);
			obj=clazz.newInstance();
			result=call(obj,method,args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object call(Class<?> cls,String method,Object[] args){
		Object result=null;
		Object obj=null;
		try {
			obj=cls.newInstance();
			result=call(obj,method,args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Class<?> cls,String method,Object[] parameterValues,Class<?>... parameterTypes){
		Object result=null;
		Object obj=null;
		Method methodObj;
		try {
			obj=cls.newInstance();
			if(obj!=null){
				methodObj = obj.getClass().getMethod(method, parameterTypes);
				result=methodObj.invoke(obj,parameterValues);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Object obj,String method,Object[] parameterValues,Class<?>... parameterTypes){
		Object result=null;
		Method methodObj;
		try {
			if(obj!=null){
				methodObj = obj.getClass().getMethod(method, parameterTypes);
				result=methodObj.invoke(obj,parameterValues);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Object obj,Method method,Object[] parameterValues,Class<?>... parameterTypes){
		Object result=null;
		try {
			if(obj!=null){
				result=method.invoke(obj,parameterValues);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Object call(Object obj,String method,Object[] args){
		Object result=null;
		Class<?>[] parameterTypes=null;
		if(args==null)
			return null;
		parameterTypes=new Class[args.length];
		for(int i=0;i<parameterTypes.length;i++){
			parameterTypes[i]=args[i].getClass();
		}
		Method methodObj;
		try {
			if(obj!=null){
				methodObj = obj.getClass().getMethod(method, parameterTypes);
				result=methodObj.invoke(obj,args);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			try{
				for(int i=0;i<parameterTypes.length;i++){
					Class<?>[] clsInterfaces=args[i].getClass().getInterfaces();
					if(clsInterfaces!=null&&clsInterfaces.length>0){
						parameterTypes[i]=clsInterfaces[0];
					}
				}
				methodObj = obj.getClass().getMethod(method, parameterTypes);
				result=methodObj.invoke(obj,args);
			}catch (NoSuchMethodException ex) {
				ex.printStackTrace();
			}catch (InvocationTargetException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
}
