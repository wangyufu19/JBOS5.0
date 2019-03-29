package com.jbosframework.jdbc.support;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.jbosframework.jdbc.support.SqlParameterConverter;

/**
 * SQL语句参数拼装类
 * 
 * @author youfu.wang
 * @version 1.0
 */
public class SqlAutowireFactory {
	public static final String JSQL_VALUES_WILDCARD="?"; 
	public static String MYSQL="mysql";
	public static String MSSQL="mssql";
	public static String ORACLE="oracle";
	public static String KINGBASE="kingbase";
	/**
	 * 拼装SQL语句,替换通配符参数"?"
	 * 
	 * @param sql
	 * @param arg
	 * @param dialect
	 * @return
	 */
	public static String makeUp(String sql,String arg){
		if(arg==null) return sql;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == '?') {
				buf.append(getParameterValues("?",arg,MYSQL));
			} else {
				buf.append(sql.charAt(i));
			}
		}
		return buf.toString(); 
	}
	/**
	 * 拼装SQL语句,替换通配符参数"?"
	 * 
	 * @param sql
	 * @param arg
	 * @param dialect
	 * @return
	 */
	public static String makeUp(String sql,String arg,String dialect){
		if(arg==null) return sql;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == '?') {
				buf.append(getParameterValues("?",arg, dialect));
			} else {
				buf.append(sql.charAt(i));
			}
		}
		return buf.toString(); 
	}
	/**
	 * 拼装SQL语句,替换通配符参数"?"
	 * 
	 * @param sql
	 * @param args
	 * @param dialect
	 * @return
	 */
	public static String makeUp(String sql, Object[] args, String dialect) {
		if (args == null)
			return sql;
		if (args.length < 1)
			return sql;
		int index = 0;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == '?') {
				buf.append(getParameterValues("?",args[index++], dialect));
			} else {
				buf.append(sql.charAt(i));
			}
		}
		return buf.toString();
	}

	/**
	 * 拼装SQL语句
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static String makeUp(String sql, Map args, String dialect) {
		if (args == null)
			return sql;
		if (args.size() < 1)
			return sql;
		StringBuilder buf = new StringBuilder();
		String finalSql = sql;
		while (sql.indexOf("$") != -1) {
			sql = sql.substring(sql.indexOf("$"), sql.length());
			int start = sql.indexOf("$");
			sql = sql.substring(start + 1, sql.length());
			int end = sql.indexOf("$");
			String property = sql.substring(start, end);
			sql = sql.substring(end + 1, sql.length());
			Object value = args.get(property);
			finalSql = finalSql.replace("$" + property + "$",getParameterValues("$"+property+"$",value,dialect));
		}
		buf.append(finalSql + " ");
		return buf.toString();
	}

	/**
	 * 得到通配符参数值
	 * 
	 * @param arg
	 * @param dialect
	 * @return
	 */
	private static String getParameterValues(String key,Object value, String dialect) {
		if(dialect==null||"null".equals(dialect)||"".equals(dialect))
			dialect=MYSQL;
		if (value == null)
			return "''";
		if (value instanceof java.lang.Integer) {
			return String.valueOf(value);
		}else if (value instanceof java.lang.Long) {
			return String.valueOf(value);
		} else if (value instanceof java.lang.Float) {
			return String.valueOf(value);
		} else if (value instanceof java.lang.String) {
			return "'" + formatValue(String.valueOf(value)) + "'";
		} else if (value instanceof java.sql.Timestamp) {			
			if(dialect.equals(MYSQL)){
				return "'"+SqlParameterConverter.convertOfTimestamp((Timestamp) value)+"'";
			}else if (dialect.equals(ORACLE))
				return "to_date('"
						+ SqlParameterConverter.convertOfTimestamp((Timestamp) value)
						+ "','YYYY-MM-DD HH24:MI:SS')";
			else if (dialect.equals(MSSQL))
				return "convert(datetime,'"
						+ SqlParameterConverter.convertOfTimestamp((Timestamp) value)
						+ "')";
			else if (dialect.equals(KINGBASE))
				return "cast('"
						+ SqlParameterConverter.convertOfTimestamp((Timestamp) value)
						+ "' as timestamp)";
		}else if (value instanceof java.sql.Date||value instanceof java.util.Date) {			
			if(dialect.equals(MYSQL)){
				return "'"+SqlParameterConverter.convertOfDate((Date) value)+"'";
			}else if (dialect.equals(ORACLE))
				return "to_date('"
						+ SqlParameterConverter.convertOfDate((Date) value)
						+ "','YYYY-MM-DD')";
			else if (dialect.equals(MSSQL))
				return "convert(datetime,'"
						+ SqlParameterConverter.convertOfDate((Date) value) + "')";
			else if (dialect.equals(KINGBASE))
				return "cast('" + SqlParameterConverter.convertOfDate((Date) value)
						+ "' as timestamp)";
		}else {
			return value.toString();
		}
		return key;
	}

	/**
	 * 格式化字符串属性的特殊值,例如:单引号(')
	 * 
	 * @param s
	 */
	private static String formatValue(String s) {
		StringBuilder buf = new StringBuilder();
		if (s == null || "null".equals(s) || "".equals(s)) {
			return "";
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\'') {
				buf.append(s.charAt(i) + "\'");
			} else
				buf.append(s.charAt(i));
		}
		return buf.toString();
	}
	public static void main(String[] args){
		String sql="select * t_user where id=?";
		
		String[] arg=new String[1];
		arg[0]="'d";
		sql=SqlAutowireFactory.makeUp(sql, arg, null);
		System.out.println("******sql: "+sql);
	}
}
