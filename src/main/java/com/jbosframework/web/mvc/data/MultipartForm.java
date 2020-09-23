package com.jbosframework.web.mvc.data;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import com.jbosframework.common.utils.CoderUtils;

/**
 * MultipartForm
 * @author youfu.wang
 * @version 1.0
 */
public class MultipartForm {	

	private HttpServletRequest request;
	private Map<String,String> parameterValues=new LinkedHashMap<String,String>();
	private Charset charset;
	
	public MultipartForm(Represention represention){
		this.request=represention.getRequest();	
		this.charset=new Charset();
	}
	public MultipartForm(HttpServletRequest request){
		this.request=request;	
		this.charset=new Charset();
	}
	public void handleMulitpartForm(MultipartFormResolver multipartFormResolver){				
		parameterValues=multipartFormResolver.handleMulitpartForm(request);
	}
	public String get(String name){
		String value="";
		value=parameterValues.get(name);
		if(value==null||"".equals(value)||"null".equals(value))
			return "";			
		return CoderUtils.decode(value,charset.getCharset());
	}
}
