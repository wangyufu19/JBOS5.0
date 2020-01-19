package com.jbosframework.utils;
/**
 * 字符串帮助类
 * @author youfu.wang
 * @version 1.0
 */
public class StringUtils {
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
	/**
	 * 拆分字符串
	 * @param s
	 * @param ch
	 * @return
	 */
	public static String[] split(String s,char ch){
		String[] strArray=null;	
		if(s==null||"".equals(s)) return strArray;				
		if(s.indexOf(ch)!=-1){		
			int num=0;		
			for(int i=0;i<s.length();i++){
				if(s.charAt(i)==ch){
					num++;
				}
			}					
			strArray=new String[num];						
			for(int i=0;i<strArray.length;i++){		
				if(s.indexOf(ch)!=-1){
					strArray[i]=s.substring(0, s.indexOf(ch));						
					s=s.substring(s.indexOf(ch)+1, s.length());			
				}else
					strArray[i]=s.substring(0, s.length());
			}
		}else{
			strArray=new String[1];
			strArray[0]=s.substring(0, s.length());
		}
		return strArray;
	}
}
