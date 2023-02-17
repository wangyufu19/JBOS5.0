package com.jbosframework.web.utils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import com.jbosframework.utils.ObjectUtils;
import com.jbosframework.web.servlet.JBOSServlet;


import javax.servlet.http.HttpServletRequest;

/**
 * CoderUtils
 * @author youfu.wang
 * @version 1.0
 */
public class WebUtils {
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";

	private static final String ISO_ENCODING="iso-8859-1";
	private static final String SERVER_TOMCAT="Tomcat";

	public static String getRequestPath(HttpServletRequest request) {
		String s = null;
		s = (String) request
				.getAttribute("javax.servlet.include.path_info");
		if (s == null)
			s = request.getPathInfo();
		if (s != null && s.length() >= 0) {
			return s;
		}
		s = (String)request.getAttribute("javax.servlet.include.servlet_path");
		if (s == null)
			s = request.getServletPath();
		return s;
	}

	public static boolean isMultipartRequest(HttpServletRequest request){
		String contentType = request.getContentType();
		if (contentType != null) {
			if (contentType.indexOf("multipart/form-data") != -1) {
				return true;
			}
		}
		return false;
	}

	public static String encode(String s) {
		return encode(s,"UTF-8");
	}
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
		if(str==null||"".equals(str.equals(""))) return str;
		try {
			value=URLDecoder.decode(str, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getUrlParameter(String url, Map<String, Object> params) {
		String s = url;
		if (s != null) {
			boolean containsQuestionMark = s.contains("?");
			StringBuffer buf = new StringBuffer(s);
			StringBuffer query = new StringBuffer(format(params.entrySet(),
					"UTF-8"));
			if (!"".equals(query.toString())) {
				if (containsQuestionMark) {
					buf.append("&").append(query);
				} else {
					buf.append("?").append(query);
				}
			}
			s = buf.toString();
		}
		return s;
	}

	public static String format(
			final Collection<? extends Map.Entry<String, Object>> parameters,
			final String encoding) {
		final StringBuilder result = new StringBuilder();
		for (final Map.Entry<String, Object> parameter : parameters) {
			String value = parameter.getValue() == null ? null : String
					.valueOf(parameter.getValue());
			if (!"".equals(parameter.getKey())
					&& !"".equals(value)) {
				final String encodedName = encode(parameter.getKey(), encoding);
				final String encodedValue = value != null ? encode(value,
						encoding) : "";
				if (result.length() > 0) {
					result.append(PARAMETER_SEPARATOR);
				}
				result.append(encodedName);
				result.append(NAME_VALUE_SEPARATOR);
				result.append(encodedValue);
			}
		}
		return result.toString();
	}
	private static boolean isTomcat(){
		if(JBOSServlet.SERVER_INFO.toLowerCase().indexOf(SERVER_TOMCAT)!=-1){
			return true;
		}else{
			return false;
		}
	}
}
