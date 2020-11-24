package com.jbosframework.web.context;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.web.context.WebAnnotationApplicationContext;
/**
 * ContextLoaderServlet
 * @author youfu.wang
 * @version 1.0
 */
public class ContextLoaderServlet extends HttpServlet{

	private static String defaultAppConfig;
	public static final String APPLICATION_CONTEXT_ATTRIBUTE="APPLICATION_CONTEXT";
	
	public ContextLoaderServlet(){
		defaultAppConfig="com.AppConfig";		
	}

	public void destory(){		
		ApplicationContextFactory.getApplicationContext().destroy();
	}
	public void init(){
		ServletContext servletContext=super.getServletConfig().getServletContext();
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

}
