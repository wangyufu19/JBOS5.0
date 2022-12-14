package com.jbosframework.jdbc.support.type;
import com.jbosframework.utils.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Locale;

/**
 * TypeConverter
 * @author youfu.wang
 * @version 1.0
 */
public class TypeConverter {
	public static final String DATA_TYPE_INTEGER="java.lang.Integer";
	public static final String DATA_TYPE_INTEGER_ALIAS="Integer";
	public static final String DATA_TYPE_LONG="java.lang.Long";
	public static final String DATA_TYPE_LONG_ALIAS="Long";
	public static final String DATA_TYPE_FLOAT="java.lang.Float";
	public static final String DATA_TYPE_FLOAT_ALIAS="Float";
	public static final String DATA_TYPE_DOUBLE="java.lang.Double";
	public static final String DATA_TYPE_DOUBLE_ALIAS="Double";	
	public static final String DATA_TYPE_CHARACTER="java.lang.Character";
	public static final String DATA_TYPE_CHARACTER_ALIAS="Character";
	public static final String DATA_TYPE_STRING="java.lang.String";
	public static final String DATA_TYPE_STRING_ALIAS="String";
	public static final String DATA_TYPE_DATE="java.lang.Date";
	public static final String DATA_TYPE_DATE_ALIAS="Date";
	public static final String DATA_TYPE_TIMESTAMP="java.sql.Timestamp";
	public static final String DATA_TYPE_TIMESTAMP_ALIAS="Timestamp";

	public static Object convert(String type,String value){
	    if(StringUtils.isNUll(value)){
	    	return value;
		}
		if(java.lang.Integer.class.getName().equals(type)||java.lang.Integer.class.getSimpleName().equals(type)||"int".equals(type)){
			return Integer.parseInt(value);
		}else if(java.lang.Long.class.getName().equals(type)||java.lang.Long.class.getSimpleName().equals(type)||"long".equals(type)){
			return Long.parseLong(value);
		}else if(java.lang.Boolean.class.getName().equals(type)||java.lang.Boolean.class.getSimpleName().equals(type)||"boolean".equals(type)){
			return Boolean.parseBoolean(value);
		}else {
			return value;
		}
	}
	public static Integer convertToInteger(Object obj){
		if(obj==null||obj.equals("")) return null;
		return Integer.valueOf(String.valueOf(obj));		
	}
	public static Long convertToLong(Object obj){
		if(obj==null||obj.equals("")) return null;
		return Long.valueOf(String.valueOf(obj));		
	}
	public static Float convertToFloat(Object obj){
		if(obj==null||obj.equals("")) return null;
		return Float.valueOf(String.valueOf(obj));		
	}
	public static Double convertToDouble(Object obj){
		if(obj==null||obj.equals("")) return null;
		return Double.valueOf(String.valueOf(obj));		
	}
	public static String convertToString(Object obj){
		if(obj==null||obj.equals("")) return null;
		return String.valueOf(obj);
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
	public static Timestamp convertToTimestamp(Object obj) {
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
	 * 判断一个类是否常用类类型
	 * @param className
	 * @return
	 */
	public static boolean isPrimitiveType(String className){
		if(className==null)
			return false;
		if(TypeConverter.DATA_TYPE_INTEGER.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_LONG.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_FLOAT.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_DOUBLE.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_CHARACTER.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_STRING.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_DATE.equals(className)){
			return true;
		}else if(TypeConverter.DATA_TYPE_TIMESTAMP.equals(className)){
			return true;
		}
		return false;
	}
	/**
	 * 判断一个类是否常用类类型
	 * @param object
	 * @return
	 */
	public static boolean isPrimitiveType(Object object){
		if(object==null)
			return false;
		return isPrimitiveType(object.getClass().getName());
	}
	/**
	 * 判断一个类是否常用类类型
	 * @param parameterClass
	 * @return
	 */
	public static boolean isPrimitiveType(Class parameterClass){
		if(parameterClass==null)
			return false;
		return isPrimitiveType(parameterClass.getName());
	}
}
