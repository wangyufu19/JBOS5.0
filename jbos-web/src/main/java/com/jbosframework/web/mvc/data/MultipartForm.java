package com.jbosframework.web.mvc.data;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;

import com.jbosframework.web.context.Charset;
import com.jbosframework.web.utils.WebUtils;

/**
 * MultipartForm
 * @author youfu.wang
 * @version 1.0
 */
public class MultipartForm {	

	private HttpServletRequest request;
	private Map<String,String> parameterValues=new LinkedHashMap<String,String>();
	private Charset charset;
	
	public MultipartForm(Representation represention){
		this.request=represention.getRequest();	
		this.charset=new Charset();
	}
	public MultipartForm(HttpServletRequest request){
		this.request=request;	
		this.charset=new Charset();
	}
	public void handleMultipartForm(MultipartFormResolver multipartFormResolver){
		parameterValues=multipartFormResolver.handleMulitpartForm(request);
	}
	public String get(String name){
		String value="";
		value=parameterValues.get(name);
		if(value==null||"".equals(value)||"null".equals(value))
			return "";			
		return WebUtils.decode(value,charset.getCharset());
	}
}
