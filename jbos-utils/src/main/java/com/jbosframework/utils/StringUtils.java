package com.jbosframework.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * 字符串帮助类
 * @author youfu.wang
 * @version 1.0
 */
public class StringUtils {
	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}

	public static boolean hasLength(String str) {
		return str != null && !str.isEmpty();
	}
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
	 * 判断非空值
	 * @param str
	 * @return
	 */
	public static boolean isNotNUll(String str){
		if(null==str||"null".equals(str)||"".equals(str)){
			return false;
		}else
			return true;
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
	public static String stringArrayToTokenize(String[] array, String delimiters) {
		if(isNUll(array)){
			return null;
		}
		String tokenizeString="";
		for(String str:array){
			tokenizeString+=tokenizeString+str+delimiters;
		}
		return tokenizeString;
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
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
			int index = inString.indexOf(oldPattern);
			if (index == -1) {
				return inString;
			} else {
				int capacity = inString.length();
				if (newPattern.length() > oldPattern.length()) {
					capacity += 16;
				}

				StringBuilder sb = new StringBuilder(capacity);
				int pos = 0;

				for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
					sb.append(inString.substring(pos, index));
					sb.append(newPattern);
					pos = index + patLen;
				}

				sb.append(inString.substring(pos));
				return sb.toString();
			}
		} else {
			return inString;
		}
	}
	public static void main(String[] ags){
		String s=new String("123");
		System.out.println(new StringUtils().toString());
	}
}
