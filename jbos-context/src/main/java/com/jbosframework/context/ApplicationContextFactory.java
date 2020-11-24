package com.jbosframework.context;
import com.jbosframework.context.support.AnnotationApplicationContext;
/**
 * ApplicationContextFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-18
 */
public class ApplicationContextFactory {
	
	private static ApplicationContext applicationContext=null;

	/**
	 * 设置应用上下文
	 * @param context
	 */
	public static void setApplicationContext(ApplicationContext context){
		if(applicationContext==null){
			synchronized(ApplicationContext.class){
				if(applicationContext==null){
					applicationContext=context;
				}
			}
		}
	}
	/**
	 * 得到ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		if(applicationContext==null){
			synchronized(ApplicationContext.class){
				if(applicationContext==null){
					applicationContext=new AnnotationApplicationContext();
				}
			}
		}
		return applicationContext;
	}
	/**
	 * 得到ApplicationContext
	 * @param cls
	 * @return
	 */
	public static ApplicationContext getApplicationContext(Class<?> cls){
		if(cls==null){
			return applicationContext;
		}
		if(applicationContext==null){
			synchronized(ApplicationContext.class){
				if(applicationContext==null){
					applicationContext=new AnnotationApplicationContext(cls);
				}
			}
		}
		return applicationContext;
	}
	/**
	 * 得到ApplicationContext
	 * @param clses
	 * @return
	 */
	public static ApplicationContext getApplicationContext(Class<?>[] clses){
		if(clses==null){
			return applicationContext;
		}
		if(applicationContext==null){
			synchronized(ApplicationContext.class){
				if(applicationContext==null){
					applicationContext=new AnnotationApplicationContext(clses);
				}
			}
		}
		return applicationContext;
	}
	
}
