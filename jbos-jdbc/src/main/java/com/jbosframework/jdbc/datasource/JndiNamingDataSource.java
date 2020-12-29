package com.jbosframework.jdbc.datasource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Jndi数据源实现类
 * @author youfu.wang
 * @version 1.0
 */
public class JndiNamingDataSource implements DataSource{
	private String jndiName;

	
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;	
	}
	/**
	 * 得到数据库连接
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException{
		Connection connection=null;
		Context initCtx=null;
		DataSource dataSource=null;
		try {
			initCtx = new InitialContext();
			dataSource=(DataSource) initCtx.lookup(this.getJndiName());
		} catch (NamingException e) {				
			e.printStackTrace();
		};		
		if(dataSource!=null){
			try {
				connection=dataSource.getConnection();
			} catch (SQLException e) {
				throw e;
			}				
		}	
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
