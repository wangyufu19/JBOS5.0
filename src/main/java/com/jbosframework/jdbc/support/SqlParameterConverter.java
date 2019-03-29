package com.jbosframework.jdbc.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

/**
 * 数据类型转换类
 * 
 * @author youfu.wang
 * @version 1.0
 */
public class SqlParameterConverter {
	
	public static boolean isModelObject(String type){		
		if(type==null) return false;
		if(type.equals("List")){
			return false;
		}else if(type.equals("String")){
			return false;
		}else if(type.equals("Map")){
			return false;
		}else if(type.equals("Iterator")){
			return false;
		}else if(type.equals("String[]")){
			return false;
		}else
			return true;	
	}
	
	
	public static Object convert(Object obj,String type){				
		if(type.equals("int")){
			if(obj==null) return 0;
			int val=Integer.valueOf(obj.toString()).intValue();			
			return val;
		}else if(type.equals("Integer")){
			return Integer.valueOf(obj.toString());
		}else if(type.equals("String")){
			return convertToString(obj);
		}else if(type.equals("long")){
			if(obj==null) return 0;
			int val=Long.valueOf(obj.toString()).intValue();			
			return val;
		}else if(type.equals("Long")){
			return convertToLong(obj);
		}else if(type.equals("Date")){
			return convertToDate(obj);
		}else if(type.equals("Timestamp")){
			return convertOfTimestamp(obj);
		}
		return obj;
	}
	public static String convertToString(Object obj){
		if(obj==null||obj.equals("")) return null;
		return String.valueOf(obj);
	}
	public static Long convertToLong(Object obj){
		if(obj==null||obj.equals("")) return null;
		return Long.valueOf(String.valueOf(obj));		
	}
	
	public static Date convertToDate(Object obj){
		if(obj==null||obj.equals("")) return null;
		Date dt = null;
        DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        try {
            dt = new Date(dtFmt.parse(obj.toString()).getTime());
        } catch (ParseException e) {           
            e.printStackTrace();
        }
        return dt;
	}
	/**
	 * 转换日期对象为"yyyy-MM-dd"格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String convertOfDate(Date date) {
		if (date == null)
			return null;
		DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return dtFmt.format(date);
	}

	/**
	 * 转换日期对象为指定格式的字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertOfDate(Date date, String pattern) {
		if (date == null)
			return null;
		DateFormat dtFmt = new SimpleDateFormat(pattern, Locale.CHINA);
		return dtFmt.format(date);
	}
	/**
	 * 转换时间对象为"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Timestamp convertOfTimestamp(Object obj) {
        if (obj== null ||obj.equals("")) return null;
        Timestamp dt = null;
        DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        try {
            dt = new Timestamp(dtFmt.parse(obj.toString()).getTime());
        } catch (ParseException e) {            
            e.printStackTrace();
        }
        return dt;
    }
	/**
	 * 转换时间对象为"yyyy-MM-dd HH:mm:ss"格式的字符串
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String convertOfTimestamp(Timestamp timestamp) {
		if (timestamp == null)
			return null;
		DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);		
		return dtFmt.format(timestamp);
	}

	/**
	 * 转换时间对象为指定格式的字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertOfTimestamp(Timestamp timestamp, String pattern) {
		if (timestamp == null)
			return null;
		DateFormat dtFmt = new SimpleDateFormat(pattern, Locale.CHINA);
		return dtFmt.format(timestamp);
	}
}
