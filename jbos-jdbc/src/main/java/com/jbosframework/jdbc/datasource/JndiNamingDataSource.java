package com.jbosframework.jdbc.datasource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Jndi数据源实现类
 * @author youfu.wang
 * @version 1.0
 */
public class JndiNamingDataSource extends JDBCDataSource{
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
}
