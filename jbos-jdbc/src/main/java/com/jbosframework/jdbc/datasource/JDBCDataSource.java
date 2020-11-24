package com.jbosframework.jdbc.datasource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;
/**
 * JDBCDataSource
 * @author youfu.wang
 * @version 1.0
 */
public abstract class JDBCDataSource implements DataSource {
	/**
	 * ORACLE数据库
	 */
	public final static String DB_ORALCE="oracle";
	/**
	 * MYSQL数据库
	 */
	public final static String DB_MYSQL="mysql";
	/**
	 * MYSQL数据库
	 */
	public final static String DB_MSSQL="mssql";
	private String dialect;
	private String showSql;
	
	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}	
	
}
