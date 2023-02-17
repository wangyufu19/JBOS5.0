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
//		System.out.println("Origin="+request.getHeader("Origin"));
//		System.out.println("method="+request.getMethod().toUpperCase());
//		System.out.println("Access-Control-Request-Headers="+request.getHeader("Access-Control-Request-Headers"));
//		System.out.println("Access-Control-Request-Method="+request.getHeader("Access-Control-Request-Method"));
//		if(this.isCorsRequest(request)){
//			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//			response.setHeader("Access-Control-Allow-Methods", "*");
//			response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//			response.setHeader("Access-Control-Allow-Credentials", "true");
//			response.setHeader("Access-Control-Max-Age", "3600");
//		}
//		if(this.isPreFlightRequest(request)){
//			return;
//		}
		this.doDispatch(request, response);		
	}
	private boolean isCorsRequest(HttpServletRequest request){
		return request.getHeader("Origin")!=null;
	}
	private boolean isPreFlightRequest(HttpServletRequest request){
		return isCorsRequest(request)&&"OPTIONS".equals(request.getMethod().toUpperCase())&&request.getHeader("Access-Control-Request-Method")!=null;
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
