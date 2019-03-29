package com.jbosframework.web.mvc.data;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.utils.CoderUtils;
import com.jbosframework.web.mvc.data.Charset;
import com.jbosframework.web.mvc.dispatcher.Dispatcher;
/**
 * Represention
 * @author youfu.wang
 * @version 1.0
 */
public class Represention {
	private ServletContext servletContext;
	private ApplicationContext applicationContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Charset charset;
	public static final String exec_success="成功";
	public static final String exec_failure="操作失败";
	
	public Represention(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;		
		this.charset=new Charset();
	}
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
	}
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}
	public HttpServletRequest getRequest(){
		return this.request;
	}
	public HttpServletResponse getResponse(){
		return this.response;
	}
	public String getParameter(String name,boolean decode){
		String value="";
		value=request.getParameter(name);	
		if(value==null||"".equals(value)||"null".equals(value))
			return "";	
		if(decode)
			value=CoderUtils.decode(value,charset.getCharset());
		return value;
	}
	public String[] getParameterValues(String name,boolean decode){
		String[] arrs=null;
		arrs=request.getParameterValues(name);
		if(arrs==null) return null;
		for(int i=0;i<arrs.length;i++){		
			if(decode)
				arrs[i]=CoderUtils.decode(arrs[i],charset.getCharset());		
		}
		return arrs;
	}	
	public Dispatcher getDispatcher(){		
		Dispatcher dispatcher=new Dispatcher(this.request,this.response);
		return dispatcher;
	}
}
