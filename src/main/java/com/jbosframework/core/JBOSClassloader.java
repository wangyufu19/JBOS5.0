package com.jbosframework.core;

import java.lang.reflect.Method;

/**
 * JBOSClassloader
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSClassloader {
	static Class<?> com$jbosframework$core$JBOSClassloader;/* synthetic field */
	
	/**
	 * 得到一个类加载器
	 * @return
	 */
	public static ClassLoader getClassLoader(){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		if (classloader == null) {
			try {
				classloader = (com$jbosframework$core$JBOSClassloader != null ? com$jbosframework$core$JBOSClassloader : (com$jbosframework$core$JBOSClassloader = class$("com.jbosframework.core.JBOSClassloader"))).getClassLoader();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classloader;
	}
	/**
	 * 加载类
	 * @param s
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String s)throws ClassNotFoundException {
		return getClassLoader().loadClass(s);
	}

	/**
	 * 得到类方法
	 * @param cls
	 * @param method
	 * @param parameterTypes
	 * @return
	 */
	public static Method getMethod(Class<?> cls,String method,Class<?>... parameterTypes){
		try {
			return cls.getMethod(method,parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 实例化
	 * @param cls
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object newInstance(Class cls){
		Object obj=null;
		try {
			obj=cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 实例化
	 * @param s
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object newInstance(String s) throws ClassNotFoundException{
		Class<?> cls=loadClass(s);
		Object obj=null;
		try {
			obj=cls.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 查询类
	 * @param s
	 * @return
	 */
	static Class<?> class$(String s) throws ClassNotFoundException {
		try {
			return Class.forName(s);
		} catch (ClassNotFoundException classnotfoundexception) {			
			throw classnotfoundexception;
		}
	}

	/**
	 * classpath下是否存在该类
	 * @param s
	 * @return
	 */
	static boolean isPresent(String s){
		try {
			class$(s);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
