package com.jbosframework.jdbc.datasource;
import com.jbosframework.transaction.DefaultTransactionStatus;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.transaction.TransactionStatus;
import com.jbosframework.transaction.support.TransactionSynchronizationManager;
import com.jbosframework.utils.Assert;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
/**
 * 事务管理
 * @author youfu.wang
 * @version 5.0
 */
public class DataSourceTransactionManager implements TransactionManager {
	private DataSource dataSource;
	/**
	 * 构造方法
	 */
	public DataSourceTransactionManager(){

	}
	/**
	 * 构造方法
	 * @param dataSource
	 */
	public DataSourceTransactionManager(DataSource dataSource){
		this.dataSource=dataSource;		
	}
	/**
	 * 设置数据源
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource=dataSource;				
	}
	/**
	 * 得到数据源
	 * @return
	 */
	public DataSource getDataSource(){
		Assert.notNull(this.dataSource,"The Object must be not null");
		return this.dataSource;
	}
	/**
	 * 得到一个事务
	 * @param transactionDefinition
	 * @return
	 * @throws SQLException
	 */
	public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws SQLException {
		DefaultTransactionStatus transactionStatus=new DefaultTransactionStatus();
		this.begin(transactionStatus,transactionDefinition);
		return transactionStatus;
	}

	/**
	 * 开始事务
	 * @param transactionDefinition
	 * @throws SQLException
	 */
	private void begin(DefaultTransactionStatus transactionStatus,TransactionDefinition transactionDefinition) throws SQLException {
		DataSourceTransactionManager.DataSourceTransactionObject txObject=new DataSourceTransactionManager.DataSourceTransactionObject();
		ConnectionHolder connectionHolder=txObject.getConnectionHolder(this.getDataSource(),true);
		Connection connection=connectionHolder.getConnection();
		if(connection!=null){
			connection.setTransactionIsolation(transactionDefinition.getIsolationLevel());
			connection.setAutoCommit(false);
		}
		transactionStatus.setConnectionHolder(connectionHolder);
	}
	/**
	 * 提交事务
	 * @param transactionStatus
	 */
	public void commit(TransactionStatus transactionStatus) {
		Connection connection=null;
		DefaultTransactionStatus defaultTransactionStatus=(DefaultTransactionStatus)transactionStatus;
		try {
			ConnectionHolder connectionHolder=(ConnectionHolder)defaultTransactionStatus.getConnectionHolder();
			connectionHolder.setTransactionActive(false);
			connection=connectionHolder.getConnection();
			if(connection!=null){
				connection.commit();
				connection.setAutoCommit(true);
			}
			TransactionSynchronizationManager.bindConnectionHolder(dataSource,connectionHolder);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.closeConnection(connection,this.getDataSource());
		}
	}
	/**
	 * 回滚事务
	 * @param transactionStatus
	 */
	public void rollback(TransactionStatus transactionStatus) {
		DefaultTransactionStatus defaultTransactionStatus=(DefaultTransactionStatus)transactionStatus;
		ConnectionHolder connectionHolder=(ConnectionHolder)defaultTransactionStatus.getConnectionHolder();
		Connection connection=connectionHolder.getConnection();
		if(connection!=null){
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DataSourceUtils.closeConnection(connection,this.getDataSource());
			}
		}
	}
	public class DataSourceTransactionObject{

		public ConnectionHolder getConnectionHolder(DataSource dataSource,boolean transactionActive) throws SQLException {
			ConnectionHolder connectionHolder=new ConnectionHolder();
			connectionHolder.setTransactionActive(transactionActive);
			connectionHolder.setConnection(dataSource.getConnection());
			TransactionSynchronizationManager.bindConnectionHolder(dataSource,connectionHolder);
			return connectionHolder;
		}
	}
}
