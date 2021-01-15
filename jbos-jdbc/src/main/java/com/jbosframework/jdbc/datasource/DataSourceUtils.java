package com.jbosframework.jdbc.datasource;
import com.jbosframework.transaction.support.TransactionSynchronizationManager;
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
	 * 连接事务
	 * @param connection
	 * @param dataSource
	 * @return
	 */
	public static boolean isConnectionTransactional(Connection connection,DataSource dataSource){
		if(dataSource==null){
			return false;
		}else{
			ConnectionHolder connectionHolder=(ConnectionHolder)TransactionSynchronizationManager.getConnectionHolder(dataSource);
			if(connectionHolder!=null){
				return connection==connectionHolder.getConnection()&&connectionHolder.isTransactionActive();
			}
		}
		return false;
	}
	/**
	 * 得到数据源
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection(DataSource dataSource) throws SQLException{
		Connection connection=null;
		ConnectionHolder connectionHolder=(ConnectionHolder)TransactionSynchronizationManager.getConnectionHolder(dataSource);
		if(connectionHolder==null){
			log.info("******Datasource connection created");
			connection=dataSource.getConnection();
			ConnectionHolder holderToUser=new ConnectionHolder();
			holderToUser.setConnection(connection);
			TransactionSynchronizationManager.bindConnectionHolder(dataSource,holderToUser);
			return connection;
		}else{
			log.info("******Fetch Datasource connection from ThreadLocal");
			return connectionHolder.getConnection();
		}
	}
	/**
	 * 关闭数据源
	 * @param dataSource
	 */
	public static void closeConnection(Connection connection,DataSource dataSource){
		ConnectionHolder connectionHolder=(ConnectionHolder)TransactionSynchronizationManager.getConnectionHolder(dataSource);
		if(connectionHolder!=null){
			Connection connectionToUse=connectionHolder.getConnection();
			if (connectionToUse != null) {
				try {
					if (!connectionToUse.isClosed()){
						if(log.isDebugEnabled()){
							log.debug("******Datasource connection closed");
						}
						connectionToUse.close();
						TransactionSynchronizationManager.removeConnectionHolder(dataSource);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}	
}
