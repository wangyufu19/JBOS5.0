package com.jbosframework.web.mvc.dispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.web.mvc.ModelAndView;
import com.jbosframework.web.mvc.data.Charset;
/**
 * Dispatcher
 * @author youfu.wang
 * @version 1.0
 */
public class Dispatcher {
	private static final int SC_404=404;
	private static final int SC_500=500;
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
	public void doDispatch(String reqeustURI,ModelAndView modelAndView) throws IOException, ServletException{
		String s="";
		if(modelAndView!=null){
			s=modelAndView.getViewName();
			if(s==null){
				doDispatcher404(reqeustURI);
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
	public void doDispatch(String reqeustURI) throws IOException, ServletException{
		String s1=null;
		if(reqeustURI!=null){
			if(reqeustURI.startsWith("/")){
	    		s1 =reqeustURI;
	    	}else
	    		s1="/"+reqeustURI;    	
		}
		doDispatcher(s1);
    }
	public void doPrintWriter(String s) throws IOException, ServletException{
		try {
			response.setContentType("text/html;charset=\""+Charset.UTF8+"\"");
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
        	return;
        } else{
            requestdispatcher.forward(request, response);
            return;
        }
    }
	
	public void doDispatcher404(String reqeustURI) throws IOException{
		String msg="The requested resource "+reqeustURI+" is not available";		
		response.sendError(SC_404,msg);   	
	}
	public void doDispatcher500(String reqeustURI) throws IOException{	
		response.sendError(SC_500,reqeustURI);   	
	}
}
