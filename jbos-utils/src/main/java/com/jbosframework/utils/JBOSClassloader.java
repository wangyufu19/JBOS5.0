package com.jbosframework.utils;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JBOSClassloader
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSClassloader {
	private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap(32);
	private static final Map<String, Class<?>> commonClassCache = new HashMap(64);

	static Class<?> com$jbosframework$core$JBOSClassloader;/* synthetic field */

	@Nullable
	public static Class<?> resolvePrimitiveClassName(@Nullable String name) {
		Class<?> result = null;
		if (name != null && name.length() <= 8) {
			result = (Class)primitiveTypeNameMap.get(name);
		}

		return result;
	}
	public static boolean isPresent(String className, @Nullable ClassLoader classLoader) {
		try {
			forName(className, classLoader);
			return true;
		} catch (IllegalAccessError var3) {
			throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + var3.getMessage(), var3);
		} catch (Throwable var4) {
			return false;
		}
	}
	public static Class<?> forName(String name, @Nullable ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
		Assert.notNull(name, "Name must not be null");
		Class<?> clazz = resolvePrimitiveClassName(name);
		if (clazz == null) {
			clazz = (Class)commonClassCache.get(name);
		}

		if (clazz != null) {
			return clazz;
		} else {
			Class elementClass;
			String elementName;
			if (name.endsWith("[]")) {
				elementName = name.substring(0, name.length() - "[]".length());
				elementClass = forName(elementName, classLoader);
				return Array.newInstance(elementClass, 0).getClass();
			} else if (name.startsWith("[L") && name.endsWith(";")) {
				elementName = name.substring("[L".length(), name.length() - 1);
				elementClass = forName(elementName, classLoader);
				return Array.newInstance(elementClass, 0).getClass();
			} else if (name.startsWith("[")) {
				elementName = name.substring("[".length());
				elementClass = forName(elementName, classLoader);
				return Array.newInstance(elementClass, 0).getClass();
			} else {
				ClassLoader clToUse = classLoader;
				if (classLoader == null) {
					clToUse = getDefaultClassLoader();
				}

				try {
					return Class.forName(name, false, clToUse);
				} catch (ClassNotFoundException var9) {
					int lastDotIndex = name.lastIndexOf(46);
					if (lastDotIndex != -1) {
						String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);

						try {
							return Class.forName(innerClassName, false, clToUse);
						} catch (ClassNotFoundException var8) {
						}
					}

					throw var9;
				}
			}
		}
	}

	@Nullable
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;

		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable var3) {
		}

		if (cl == null) {
			cl = JBOSClassloader.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable var2) {
				}
			}
		}

		return cl;
	}
	public static ClassLoader getClassLoader(){
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		if (classloader == null) {
			try {
				classloader = (com$jbosframework$core$JBOSClassloader != null ? com$jbosframework$core$JBOSClassloader : (com$jbosframework$core$JBOSClassloader = class$("com.jbosframework.utils.JBOSClassloader"))).getClassLoader();
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
	public static Object newInstance(Class cls,Class<?>[] parameterTypes,Object[] args){
		Object obj=null;
		try {
			Constructor<?> constructor = cls.getDeclaredConstructor(parameterTypes);
			obj = constructor.newInstance(args);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e){
			e.printStackTrace();;
		} catch (InvocationTargetException e) {
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
	public static boolean isPresent(String s){
		try {
			class$(s);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
