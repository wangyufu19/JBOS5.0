package com.jbosframework.core;

/**
 * JBOSClassloader
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSClassloader {
	static Class<?> com$jbosframework$core$JBOSClassloader;/* synthetic field */
	
	/**
	 * 寰楀埌褰撳墠绫诲姞杞藉櫒
	 * @return
	 */
	public static ClassLoader getClassLoader(){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		if (classloader == null)
			 classloader = (com$jbosframework$core$JBOSClassloader != null ? com$jbosframework$core$JBOSClassloader : (com$jbosframework$core$JBOSClassloader = class$("com.jbosframework.core.JBOSClassloader"))).getClassLoader();
		return classloader;
	}
	/**
	 * 鍔犺浇绫�
	 * @param s
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String s)throws ClassNotFoundException {
		return getClassLoader().loadClass(s);
	}
	/**
	 * 瀹炰緥鍖栫被
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
	 * 娉ㄥ唽绫�
	 * @param s
	 * @return
	 */
	static Class<?> class$(String s){
		try {
			return Class.forName(s);
		} catch (ClassNotFoundException classnotfoundexception) {			
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
	}
}
