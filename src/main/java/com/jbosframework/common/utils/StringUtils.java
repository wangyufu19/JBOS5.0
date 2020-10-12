package com.jbosframework.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * 字符串帮助类
 * @author youfu.wang
 * @version 1.0
 */
public class StringUtils {
	/**
	 * 判断是空值
	 * @param str
	 * @return
	 */
	public static boolean isNUll(String str){
		if(null==str||"null".equals(str)||"".equals(str)){
			return true;
		}else
			return false;
	}
	/**
	 * 判断是空值
	 * @param obj
	 * @return
	 */
	public static boolean isNUll(Object obj){
		if(null==obj){
			return true;
		}else
			return false;
	}
	/**
	 * 替换空指针
	 * @param str
	 * @return
	 */
	public static String replaceNull(String str){			
		if(str==null||str.equals("null")){			
			return "";
		}else 
			return str;			
	}
	/**
	 * 替换空指针
	 * @param obj
	 * @return
	 */
	public static String replaceNull(Object obj){		
		if(obj==null||obj.equals("null")){			
			return "";
		}else 
			return String.valueOf(obj);			
	}
	/**
	 * 替换空指针
	 * @param str
	 * @return
	 */
	public static String[] replaceNull(String[] str){
		if(str==null) return null;
		for(int i=0;i<str.length;i++){
			if(str[i]==null||str[i].equals("null")){
				str[i]="";
			}
		}
		return str;		
	}
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
		if (str == null) {
			return null;
		} else {
			StringTokenizer st = new StringTokenizer(str, delimiters);
			ArrayList tokens = new ArrayList();

			while(true) {
				String token;
				do {
					if (!st.hasMoreTokens()) {
						return toStringArray(tokens);
					}

					token = st.nextToken();
					if (trimTokens) {
						token = token.trim();
					}
				} while(ignoreEmptyTokens && token.length() <= 0);

				tokens.add(token);
			}
		}
	}
	public static String[] toStringArray(Collection collection) {
		return collection == null ? null : (String[])((String[])collection.toArray(new String[collection.size()]));
	}
}
