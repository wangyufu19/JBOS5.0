package com.jbosframework.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 日期帮助类
 * @author youfu.wang
 * @version 1.0
 */
public class DateUtils {
	/**
	 * 得到日期YYYY的值
	 * @param date
	 * @return
	 */
	public static String getYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.valueOf(calendar.get(1));
	}
	/**
	 * 得到日期MM的值
	 * @param date
	 * @return
	 */
	public static String getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.valueOf(calendar.get(2) + 1);		
	}
	/**
	 * 得到日期DD的值
	 * @param date
	 * @return
	 */
	public static String getDay(Date date){	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.valueOf(calendar.get(5));
	}
	/**
	 * 得到当前日期YYYY的值班
	 * @return
	 */
	public static String getYear(){
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(1));
	}
	/**
	 * 得到当前日期MM的值
	 * @return
	 */
	public static String getMonth(){
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(2) + 1);		
	}
	/**
	 * 得到当前日期DD的值
	 * @return
	 */
	public static String getDay(){
		Calendar calendar = Calendar.getInstance();
		return String.valueOf(calendar.get(5));
	}	
	/**
	 * 将long时间转换成Data对象 
	 * @param millis
	 * @return
	 */
	public static Date convertLongToDate(long millis){
		Calendar calendar=Calendar.getInstance();		
		calendar.setTimeInMillis(millis);		
		return calendar.getTime();
	}
	/**
	 * 将日期对象格式化为指定的模式字符串
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		if (date == null)
            return null;
	    DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
	    return dtFmt.format(date);
	}
	/**
	 * 将日期对象格式化为指定的模式字符串
	 * @param date
	 * @return
	 */
	public static String format(LocalDate date){
		if (date == null)
			return null;
		DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return date.format(dtFmt);
	}
	/**
	 * 将日期对象格式化为指定的模式字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date,String pattern){
		if (date == null)
            return null;
	    DateFormat dtFmt = new SimpleDateFormat(pattern,Locale.CHINA);
	    return dtFmt.format(date);
	}
	/**
	 * 将日期对象格式化为指定的模式字符串
	 * @param date
	 * @return
	 */
	public static String format(LocalDate date,String pattern){
		if (date == null)
			return null;
		DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern(pattern);
		return date.format(dtFmt);
	}
	/**
	 * 将字符串转换日期对象
	 * @param s
	 * @return
	 */
	public static LocalDate parse(String s){
		return LocalDate.parse(s);
	}
	/**
	 * 将long时间转换成Timestamp对象 
	 * @param millis
	 * @return
	 */
	public static java.sql.Timestamp convertLongToTimestamp(long millis){
		return new java.sql.Timestamp(millis);
	}
	/**
	 * 得到当前时间戳
	 * @return
	 */
	public static String getCurrentTimestamp(){
		String ret="";
		ret=String.valueOf(System.currentTimeMillis());
		return ret;
	}
	/**
	 * 得到当前LONG时间
	 * @return
	 */
	public static long getCurrentTime(){
		return new Date().getTime();
	}	
	/**
	 * 当前日期
	 * @return
	 */
	public static Date getCurrentDate(){
		return new Date();
	}
	/**
	 * 比较两个CALENDAR,返回一个相差值
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static int getCalendarCompareTo(long arg1,long arg2){
		int ret=0;
		Calendar calendar1=Calendar.getInstance();
		Calendar calendar2=Calendar.getInstance();
		calendar1.setTimeInMillis(arg1);
		calendar2.setTimeInMillis(arg2);
		ret=calendar1.compareTo(calendar2);
		return ret;	
	}
	public static void main(String[] args){
		System.out.println(DateUtils.format(LocalDate.of(2022,3,29)));
		System.out.println(DateUtils.parse("2022-03-29").plusDays(3));
	}
}
