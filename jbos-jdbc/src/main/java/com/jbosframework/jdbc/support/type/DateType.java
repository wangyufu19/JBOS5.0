package com.jbosframework.jdbc.support.type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期类型对象
 * @author youfu.wang
 * @version 1.0
 */
public class DateType {

	public Date getDate(Object s) {
        if (s == null || s.equals(""))
            return null;       
        Date dt = null;
        DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        try {
            dt = new Date(dtFmt.parse(s.toString()).getTime());
        } catch (ParseException e) {           
            e.printStackTrace();
        }
        return dt;
    }
	/**
	 * 将日期对象格式化为"yyyy-MM-dd"的字符串
	 * @param date
	 * @return
	 */
	public String format(Date date){
		if (date == null)
	            return null;
        DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        return dtFmt.format(date);
	}
	/**
	 * 将日期对象格式化为指定的模式字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public String format(Date date,String pattern){
		if (date == null)
            return null;
	    DateFormat dtFmt = new SimpleDateFormat(pattern,Locale.CHINA);
	    return dtFmt.format(date);
	}
	/**
	 * 得到数据库日期格式
	 * to_char('column','yyyy-mm-dd')
	 * @param s
	 * @return
	 */	
	public static String getdbyyyymmdd(String s,String dialect){
		if(JDBCDataSource.DB_ORALCE.equals(dialect)){
			return "to_char("+s+",'YYYY-MM-DD')";
		}
		return "";
	}
	/**
	 * 得到数据库日期格式
	 * to_char('column','yyyy-mm-dd hh:mi:ss')
	 * @param s
	 * @return
	 */
	public static String getdbyyyymmddhhmiss(String s,String dialect){
		if(JDBCDataSource.DB_ORALCE.equals(dialect)){
			return "to_char("+s+",'YYYY-MM-DD HH24:MI:SS')";
		}
		return "";
	}
    /**
     * 得到插入数据库日期格式
     * to_date('sysdate','yyyy-mm-dd')
     * @param s
     * @return
     */
	public static String insertdbyyyymmdd(String s,String dialect){
		if(JDBCDataSource.DB_ORALCE.equals(dialect)){
			return "to_date('"+s+"','YYYY-MM-DD')";
		}else if(JDBCDataSource.DB_MYSQL.equals(dialect)){			
			return "convert(datetime,'"+s+"')";
		}else if(JDBCDataSource.DB_MSSQL.equals(dialect)){
			
		}
		return "";	
	}
    /**
     * 得到插入数据库日期格式
     * to_date('sysdate','yyyy-mm-dd hh:mi:ss')
     * @param s
     * @return
     */
	public static String insertdbyyyymmddhhmiss(String s,String dialect){
		if(JDBCDataSource.DB_ORALCE.equals(dialect)){
			return "to_date('"+s+"','YYYY-MM-DD HH24:MI:SS')";
		}else if(JDBCDataSource.DB_MYSQL.equals(dialect)){
			return "convert(datetime,'"+s+"')"; 
		}else if(JDBCDataSource.DB_MSSQL.equals(dialect)){
			
		}
		return "";	
	}
	/**
	 * 得到插入数据库系统日期 
	 * @return
	 */
	public static String insertdbsysdate(String dialect){
		if(JDBCDataSource.DB_ORALCE.equals(dialect)){
			return "sysdate";
		}
		return "";
	}
}
