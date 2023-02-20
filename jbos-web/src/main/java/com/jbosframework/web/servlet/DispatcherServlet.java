package com.jbosframework.web.servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.web.mvc.dispatcher.DispatchHandler;
/**
 * DispatcherServlet
 * @author youfu.wang
 * @version 5.0
 */
public class DispatcherServlet extends JBOSServlet{
	
	public static final String DISPATCHER_CONTEXT_ATTRIBUTE=DispatcherServlet.class.getName()+".DISPATCHER_CONTEXT";	
	
	public void doService(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(logger.isDebugEnabled()){
			logger.debug("DispatcherServlet with name '" + getServletName() + "' received request for [" +
					request.getRequestURI() + "]");
		}
		this.doDispatch(request, response);		
	}
	protected void doDispatch(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DispatchHandler requestDispatchHandler=null;
		try{
			requestDispatchHandler=this.getRequestHandler(request, response);
			requestDispatchHandler.setServletContext(servletContext);
			requestDispatchHandler.setApplicationContext(this.getApplicationContext());
			requestDispatchHandler.handle();
		}catch(Exception ex){
			throw ex;
		}
	}	
	protected DispatchHandler getRequestHandler(HttpServletRequest request,HttpServletResponse response){
		DispatchHandler requestDispatchHandler=null;
		requestDispatchHandler=new DispatchHandler(request,response);
		return requestDispatchHandler;
	}
	
}
