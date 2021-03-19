package com.jbosframework.web.servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.context.ContextLoaderServlet;
/**
 * JBOSServlet
 * @author youfu.wang
 * @version 1.0
 */
public abstract class JBOSServlet extends HttpServlet{
	
	protected Log logger=LogFactory.getLog(this.getClass());	
	public static String SERVER_INFO="";	
	public ServletContext servletContext;
	
	public void init(){
		servletContext=super.getServletConfig().getServletContext();
		SERVER_INFO=servletContext.getServerInfo();	
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException{
		handleRequest(request,response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException{		
		handleRequest(request,response);
	}
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException{		
		try{
			doService(request,response);
		}catch (Throwable ex) {
			throw new ServletException("Request processing failed", ex);
		}
	}		
	
	public abstract void doService(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	public ServletContext getServletContext(){
		return servletContext;
	}
	public ApplicationContext getApplicationContext(){
		ApplicationContext applicationContext=(ApplicationContext)servletContext.getAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE);
		return applicationContext;
	}
}
