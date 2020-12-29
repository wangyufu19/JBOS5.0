package com.jbosframework.jdbc.datasource;
import com.jbosframework.transaction.TransactionSynchronizationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
/**
 * DataSourceUtils
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceUtils {
	private static final Log log= LogFactory.getLog(DataSourceUtils.class);
	/**
	 * 得到数据源
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection(DataSource dataSource) throws SQLException{
		Connection connection=null;
		ConnectionHolder connectionHolder=TransactionSynchronizationManager.getConnectionHolder(dataSource);
		if(connectionHolder==null){
			connection=dataSource.getConnection();
			ConnectionHolder holderToUser=new ConnectionHolder();
			holderToUser.setConnection(connection);
			TransactionSynchronizationManager.bindConnectionHolder(dataSource,holderToUser);
			return connection;
		}else{
			return connectionHolder.getConnection();
		}
	}
	/**
	 * 释放数据源
	 * @param dataSource
	 */
	public static void releaseConnection(DataSource dataSource){
		ConnectionHolder connectionHolder=TransactionSynchronizationManager.getConnectionHolder(dataSource);
		if(connectionHolder!=null){
			Connection connection=connectionHolder.getConnection();
			if (connection != null) {
				try {
					if (!connection.isClosed()&&connection.getAutoCommit()){
						if(log.isDebugEnabled()){
							log.debug("******Datasource connection closed");
						}
						connection.close();
						TransactionSynchronizationManager.removeConnectionHolder(dataSource);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
