package com.jbosframework.web.mvc.dispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jbosframework.utils.JsonUtils;
import com.jbosframework.web.context.Charset;
import com.jbosframework.web.mvc.ModelAndView;
/**
 * Dispatcher
 * @author youfu.wang
 * @version 1.0
 */
public class Dispatcher {
	public static final int SC_404=404;
	public static final int SC_500=500;
	public static final String APPLICATION_TEXT_HTML="text/html";
	public static final String APPLICATION_XML="application/xml";
	public static final String APPLICATION_JSON="application/json ";
	public static final String APPLICATION_IMAGE_GIF="image/gif";
	public static final String APPLICATION_IMAGE_JPEG="image/jpeg";
	public static final String APPLICATION_IMAGE_PNG="image/png";
	private HttpServletRequest request;
	private HttpServletResponse response;	
	/**
	 * 构造方法
	 * @param request
	 * @param response
	 */
	public Dispatcher(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}

	/**
	 * 处理响应
	 * @param requestUri
	 * @param ret
	 */
	public void doDispatch(String requestUri,String contentType, Object ret) throws IOException, ServletException {
		if(ret instanceof ModelAndView){
			doDispatch(requestUri,(ModelAndView)ret);
		}else{
			if(ret instanceof ModelAndView){
				doDispatch(requestUri,(ModelAndView)ret);
			}else{
				String resContent="";
				if(ret instanceof String){
					resContent=String.valueOf(ret);
				}else{
					resContent=JsonUtils.toJson(ret);
				}
				this.doPrintWriter(contentType,resContent);
			}
		}
	}
	private void doDispatch(String requestUri,ModelAndView modelAndView) throws IOException, ServletException{
		String s="";
		if(modelAndView!=null){
			s=modelAndView.getViewName();
			if(s==null){
				doDispatcher404(requestUri);
				return;
			}
			Map<String,Object> modelObject=modelAndView.getModelObjects();
			for (Map.Entry<String, Object> entry : modelObject.entrySet()) {
				this.request.setAttribute(entry.getKey(), entry.getValue());
			}
		}		
		
		String s1=null;
    	if(s.startsWith("/")){
    		s1 =s;
    	}else
    		s1="/"+s;    	
    	doDispatcher(s1);
	}
	private void doDispatch(String requestUri) throws IOException, ServletException{
		String s1=null;
		if(requestUri!=null){
			if(requestUri.startsWith("/")){
	    		s1 =requestUri;
	    	}else
	    		s1="/"+requestUri;
		}
		doDispatcher(s1);
    }
	private void doPrintWriter(String contentType,String s){
		try {
			response.setContentType(contentType+";charset=\""+Charset.UTF8+"\"");
			PrintWriter out = response.getWriter();
			out.write(s);
	        out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}	
	private void doDispatcher(String s) throws IOException, ServletException{
		RequestDispatcher requestdispatcher = request.getRequestDispatcher(s);
        if(requestdispatcher == null){
        	doDispatcher500(s);
		} else{
            requestdispatcher.forward(request, response);
		}
		return;
	}
	
	public void doDispatcher404(String requestURI) throws IOException{
		String msg="The requested resource "+requestURI+" is not available";
		response.sendError(SC_404,msg);   	
	}
	public void doDispatcher500(String requestURI) throws IOException{
		response.sendError(SC_500,requestURI);
	}
}
