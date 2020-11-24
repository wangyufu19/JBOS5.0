package com.jbosframework.web.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.web.mvc.dispatcher.DispatchHandler;
import com.jbosframework.web.servlet.JBOSServlet;
/**
 * DispatcherServlet
 * @author youfu.wang
 * @version 1.0
 */
public class DispatcherServlet extends JBOSServlet{
	
	public static final String DISPATCHER_CONTEXT_ATTRIBUTE=DispatcherServlet.class.getName()+".DISPATCHER_CONTEXT";	
	
	public void doService(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(logger.isDebugEnabled()){
			logger.debug("DispatcherServlet with name '" + getServletName() + "' received request for [" +
					request.getRequestURI() + "]");
		}
		//处理控制器分发
		this.doDispatch(request, response);		
	}
	protected void doDispatch(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DispatchHandler requestDispatchtHandler=null;
		try{
			requestDispatchtHandler=this.getRequestHandler(request, response);
			requestDispatchtHandler.setServletContext(servletContext);
			requestDispatchtHandler.setApplicationContext(this.getApplicationContext());
			requestDispatchtHandler.handle();			
		}catch(Exception ex){
			throw ex;
		}
	}	
	protected DispatchHandler getRequestHandler(HttpServletRequest request,HttpServletResponse response){
		DispatchHandler requestDispatchtHandler=null;
		requestDispatchtHandler=new DispatchHandler(request,response);
		return requestDispatchtHandler;
	}
	
}
