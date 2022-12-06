package com.jbosframework.context;
import com.jbosframework.context.annotation.AnnotationConfigApplicationContext;
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
					applicationContext=new AnnotationConfigApplicationContext();
				}
			}
		}
		return applicationContext;
	}
}
