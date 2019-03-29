package com.jbosframework.jdbc.datasource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.jbosframework.jdbc.core.Transaction;
import com.jbosframework.jdbc.datasource.DataSourceUtils;
/**
 * 事务管理
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceTransactionManager implements Transaction{
	private Connection connection=null;
	private DataSource dataSource=null;
	private boolean committed = false;
	private boolean rolledBack = false;
	/**
	 * 构造方法
	 * @param dataSource
	 */
	public DataSourceTransactionManager(DataSource dataSource){
		this.dataSource=dataSource;		
	}
	/**
	 * 设置数据源连接
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource=dataSource;				
	}
	/**
	 * 得到数据源连接
	 * @return
	 */
	public DataSource getDataSource(){
		return this.dataSource;
	}
	/**
	 * 开始事务
	 * @throws SQLException 
	 */
	public void begin() throws SQLException{
		connection=DataSourceUtils.getConnection(dataSource);
		if(connection!=null){
			connection.setAutoCommit(false);
		}
	}
	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public void commit() throws SQLException {		
		if(connection!=null){
			connection.commit();		
			connection.setAutoCommit(true);	
			this.committed = connection.getAutoCommit();	
		}
	}
	/**
	 * 回滚事务
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		if(connection!=null){
			connection.rollback();
			connection.setAutoCommit(true);	
			this.rolledBack = true;
		}
	}
	/**
	 * 事务是否已经提交
	 * @return
	 */
	public boolean wasCommitted() {
		return this.committed;
	}
	/**
	 * 事务是否已经回滚
	 * @return
	 */
	public boolean wasRolledBack() {
		return this.rolledBack;
	}
}
