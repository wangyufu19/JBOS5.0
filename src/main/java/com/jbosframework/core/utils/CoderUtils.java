package com.jbosframework.core.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.jbosframework.web.servlet.JBOSServlet;
/**
 * CoderUtils
 * @author youfu.wang
 * @version 1.0
 */
public class CoderUtils {
	private static final String ISO_ENCODING="iso-8859-1";
	private static final String SERVER_TOMCAT="tomcat";
	
	public static String encode(String s,String charset) {
		if (s==null) return "";
		try {
			s = URLEncoder.encode(s,charset);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return s;
	}
	public static String decode(String str,String charset){
		String value="";
		if(str==null||"".equals(str)) return str;		
		try {
			if(isTomcat()){			
				value=new String(str.getBytes(ISO_ENCODING),charset);
			}else
				value=URLDecoder.decode(str, charset);
			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return value;
	}
	private static boolean isTomcat(){
		
		if(JBOSServlet.SERVER_INFO.toLowerCase().indexOf(SERVER_TOMCAT)!=-1){
			return true;
		}else{
			return false;
		}
	}
}
