package com.jbosframework.jdbc.datasource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	private static final Log log= LogFactory.getLog(DataSourceUtils.class);
	/**
	 * 数据源本地线程副本变量
	 */
	private static ThreadLocal<DataSource> dataSources=new ThreadLocal<DataSource>();
	/**
	 * 得到数据源连接
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection(DataSource dataSource) throws SQLException{
		Connection connection=null;
		DataSource currentDataSource=dataSources.get();
		if(currentDataSource==null){
			connection=doConnection(dataSource);
			dataSources.set(dataSource);
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
	public static void releaseConnection(Connection connection,DataSource dataSource){
		DataSource currentDataSource=dataSources.get();
		if (connection != null&&currentDataSource!=null) {
			try {				
				if (!connection.isClosed()&&connection.getAutoCommit()){
					if(log.isDebugEnabled()){
						log.debug("******connection closed");
					}
					connection.close();
					dataSources.remove();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
}
