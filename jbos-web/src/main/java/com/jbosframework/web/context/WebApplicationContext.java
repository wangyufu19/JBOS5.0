package com.jbosframework.web.context;
import javax.servlet.ServletContext;
import com.jbosframework.context.ApplicationContext;

/**
 * WebApplicationContext
 * @author youfu.wang	
 * @version 1.0
 */
public class WebApplicationContext {
	
	public static ApplicationContext getWebApplicationContext(ServletContext servletContext){
		ApplicationContext applicationContext=(ApplicationContext)servletContext.getAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE);
		return applicationContext;
	}
	
}
