package com.jbosframework.web.mvc.data;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.context.Charset;
import com.jbosframework.web.utils.WebUtils;
import com.jbosframework.web.mvc.dispatcher.Dispatcher;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Represention
 * @author youfu.wang
 * @version 1.0
 */
public class Representation {
	private ServletContext servletContext;
	private ApplicationContext applicationContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Charset charset;
	public static final String exec_success="success";
	public static final String exec_failure="failure";
	
	public Representation(HttpServletRequest request, HttpServletResponse response){
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
			value= WebUtils.decode(value,charset.getCharset());
		return value;
	}
	public String[] getParameterValues(String name,boolean decode){
		String[] arrs=null;
		arrs=request.getParameterValues(name);
		if(arrs==null) return null;
		for(int i=0;i<arrs.length;i++){		
			if(decode)
				arrs[i]= WebUtils.decode(arrs[i],charset.getCharset());
		}
		return arrs;
	}	
	public Dispatcher getDispatcher(){		
		Dispatcher dispatcher=new Dispatcher(this.request,this.response);
		return dispatcher;
	}

	public String getRequestBody(String charset) {
	    String body="";
		InputStream inputStream=null;
		try{
			inputStream=this.getRequest().getInputStream();
            body=IOUtils.toString(inputStream,charset);
		}catch (IOException ex) {
			ex.printStackTrace();
		}finally {
            IOUtils.closeQuietly(inputStream);
		}
		return body;
	}
}
