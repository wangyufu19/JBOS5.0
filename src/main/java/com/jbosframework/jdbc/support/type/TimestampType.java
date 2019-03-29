package com.jbosframework.jdbc.support.type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
/**
 * 时间类型对象
 * @author youfu.wang
 * @version 1.0
 */
public class TimestampType {	
	/**
	 * 从数据集中得到指定列的值
	 * @param rst
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Object get(ResultSet rst,String name) throws SQLException{
		Object obj=null;
		obj=rst.getTimestamp(name);
		if(obj==null) return null;
		return parseTimestamp(obj.toString());
	}
	/**
	 * 将字符串转换成时间对象
	 * @param s
	 * @return
	 */
	private Timestamp parseTimestamp(String s) {
        if (s == null || s.equals(""))
            return null;
        Timestamp dt = null;
        DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        try {
            dt = new Timestamp(dtFmt.parse(s).getTime());
        } catch (ParseException e) {            
            e.printStackTrace();
        }
        return dt;
    }
	/**
	 * 将毫秒级时间格式化成指定格式的字符串
	 * yyyy-mm-dd hh:mm:ss
	 * @param millis
	 * @return
	 */
	public String format(Long millis){
		String timestamp="";
		Calendar calendar=Calendar.getInstance();		
		calendar.setTimeInMillis(millis);		
		int month = calendar.get(2) + 1;
		timestamp += calendar.get(1) + "-" + month + "-" + calendar.get(5) + " "
				+ calendar.get(11) + ":" + calendar.get(12) + ":"
				+ calendar.get(13);	
		return timestamp;
	}
	/**
	 * 将时间对象格式化为"yyyy-MM-dd HH:mm:ss"的字符串
	 * @param timestamp
	 * @return
	 */
	public String format(Timestamp timestamp){
		if(timestamp==null) return null;
		DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		return dtFmt.format(timestamp);		
	}
	/**
	 * 将时间对象格式化为指定的模式字符串
	 * @param timestamp
	 * @param pattern
	 * @return
	 */
	public String format(Timestamp timestamp,String pattern){
		if(timestamp==null) return null;
		DateFormat dtFmt = new SimpleDateFormat(pattern,Locale.CHINA);
		return dtFmt.format(timestamp);		
	}	

}
