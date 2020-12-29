package com.jbosframework.jdbc.datasource;
import com.jbosframework.transaction.DefaultTransactionStatus;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.transaction.TransactionStatus;
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
	private void begin(DefaultTransactionStatus transactionStatus,TransactionDefinition transactionDefinition) throws SQLException{
		DataSourceTransactionManager.DataSourceTransactionObject txObject=new DataSourceTransactionManager.DataSourceTransactionObject();
		ConnectionHolder connectionHolder=txObject.getConnectionHolder(this.dataSource,true);
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
	public void commit(TransactionStatus transactionStatus) throws SQLException {
		DefaultTransactionStatus defaultTransactionStatus=(DefaultTransactionStatus)transactionStatus;
		ConnectionHolder connectionHolder=defaultTransactionStatus.getConnectionHolder();
		Connection connection=connectionHolder.getConnection();
		if(connection!=null){
			connection.commit();
			connection.setAutoCommit(true);
		}
	}
	/**
	 * 回滚事务
	 * @param transactionStatus
	 */
	public void rollback(TransactionStatus transactionStatus) throws SQLException {
		DefaultTransactionStatus defaultTransactionStatus=(DefaultTransactionStatus)transactionStatus;
		ConnectionHolder connectionHolder=defaultTransactionStatus.getConnectionHolder();
		Connection connection=connectionHolder.getConnection();
		if(connection!=null){
			connection.rollback();
			connection.setAutoCommit(true);
		}
	}
	public class DataSourceTransactionObject{
		private ConnectionHolder connectionHolder=new ConnectionHolder();

		public DataSourceTransactionObject(){

		}

		public ConnectionHolder getConnectionHolder(DataSource dataSource,boolean transactionActive) throws SQLException {
			connectionHolder.setConnection(DataSourceUtils.getConnection(dataSource));
			connectionHolder.setTransactionActive(transactionActive);
			return connectionHolder;
		}
	}
}
