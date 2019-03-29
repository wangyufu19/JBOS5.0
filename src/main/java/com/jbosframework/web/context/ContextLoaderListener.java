package com.jbosframework.web.context;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.core.JBOSClassloader;
/**
 * ContextLoaderListener
 * @author youfu.wang
 * @date 2016-05-20
 */
public class ContextLoaderListener implements ServletContextListener{
	private static String defaultAppConfig;
	public static final String APPLICATION_CONTEXT_ATTRIBUTE="APPLICATION_CONTEXT";
	
	public ContextLoaderListener(){
		defaultAppConfig="com.AppConfig";	
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext=servletContextEvent.getServletContext();
		String appConfig=servletContext.getInitParameter("appConfig");	
		ApplicationContext applicationContext=null;
		try {
			if(appConfig==null){		
				applicationContext=ApplicationContextFactory.getApplicationContext(JBOSClassloader.loadClass(defaultAppConfig));		
			}else{
				String[] appConfigs=appConfig.split(" ");
				if(appConfigs==null){
					applicationContext=ApplicationContextFactory.getApplicationContext(JBOSClassloader.loadClass(defaultAppConfig));
				}else{		
					Class<?>[] appConfigClasses=new Class<?>[appConfigs.length];
					for(int i=0;i<appConfigs.length;i++) {
						appConfigClasses[i]=JBOSClassloader.loadClass(appConfigs[i]);
					}
					applicationContext=new WebAnnotationApplicationContext().initWebApplicationContext(appConfigClasses);
					ApplicationContextFactory.setApplicationContext(applicationContext);
				}			
			}
			servletContext.setAttribute(APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);	
		}catch(Exception e) {
			e.printStackTrace();
		}			
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ApplicationContextFactory.getApplicationContext().destroy();
	}
}
