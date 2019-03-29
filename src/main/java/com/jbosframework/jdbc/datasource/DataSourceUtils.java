package com.jbosframework.jdbc.datasource;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.ThreadLocal;
import javax.sql.DataSource;
/**
 * DataSourceUtils
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceUtils {
	/**
	 * 数据源本地线程副本变量
	 */
	private static ThreadLocal<Connection> dataSources=new ThreadLocal<Connection>();
	/**
	 * 得到数据源连接
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection(DataSource dataSource) throws SQLException{
		Connection connection=null;		
		connection=dataSources.get();
		if(connection==null){
			connection=doConnection(dataSource);
			dataSources.set(connection);
		}
		return connection;
	}
	/**
	 * 执行数据源连接
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	private static Connection doConnection(DataSource dataSource) throws SQLException{
		Connection connection=null;				
		if(dataSource==null){
			throw new SQLException("没有找到可用的数据源");
		}
		connection=dataSource.getConnection();		
		return connection;
	}
	/**
	 * 释放数据源连接
	 */
	public static void releaseConnection(){
		Connection connection=null;
		connection=dataSources.get();	
		if (connection != null) {		
			try {				
				if (!connection.isClosed()&&connection.getAutoCommit()){
//					System.out.println("******connection closed");
					connection.close();
					dataSources.remove();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
}
