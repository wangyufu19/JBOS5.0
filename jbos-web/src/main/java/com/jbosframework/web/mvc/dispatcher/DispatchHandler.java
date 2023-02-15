package com.jbosframework.web.mvc.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.servlet.DefaultHandlerChain;
import com.jbosframework.web.servlet.DefaultHandlerMapping;
import com.jbosframework.web.servlet.HandlerExecutionChain;
import com.jbosframework.web.servlet.HandlerMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DispatchHandler
 * @author youfu.wang
 * @version 1.0
 */
public class DispatchHandler {
	public static final Log logger= LogFactory.getLog(DispatchHandler.class);
	private ApplicationContext applicationContext;
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;


	public DispatchHandler(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}	

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
	}
	private HandlerExecutionChain geHandlerExecutionChain(){
		return new DefaultHandlerChain(this.applicationContext).getHandlerExecutionChain(this.request,this.response);
	}
	private HandlerMapping getHandlerMapping(){
		DefaultHandlerMapping defaultHandlerMapping=new DefaultHandlerMapping(this.servletContext);
		defaultHandlerMapping.setApplicationContext(this.applicationContext);
		return defaultHandlerMapping;
	}
	public void handle() throws Exception{
		try{
			HandlerExecutionChain handlerExecutionChain=this.geHandlerExecutionChain();
			if(!handlerExecutionChain.applyPreHandle()){
				return;
			}
			HandlerMapping handlerMapping=this.getHandlerMapping();
			handlerMapping.execute(request,response);
			handlerExecutionChain.applyPostHandle(null);
		}catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
}
