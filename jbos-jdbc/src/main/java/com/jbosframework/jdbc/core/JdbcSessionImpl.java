package com.jbosframework.jdbc.core;
import java.sql.SQLException;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.TransactionManager;

import javax.sql.DataSource;

/**
 * JDBC会话实现类
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcSessionImpl implements JdbcSession{
	private DataSource dataSource;
	/**
	 * 构造方法
	 */
	public JdbcSessionImpl(){
		
	}
	/**
	 * 构造方法
	 * @param dataSource
	 */
	public JdbcSessionImpl(DataSource dataSource){
		this.dataSource=dataSource;
	}
	/**
	 * 得到数据源对象
	 * @return
	 */
	public DataSource getDataSource(){
		return this.dataSource;
	}
	/**
	 * 设置数据源对象
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 开始一个事务
	 * @throws SQLException 
	 */
	public TransactionManager beginTransaction() throws SQLException{
		TransactionManager tx=new DataSourceTransactionManager(this.dataSource);
		return tx;
	}
	/**
	 * 得到JdbcTmplt
	 * @return
	 */
	public JdbcTmplt getJdbcTmplt(){
		JdbcTmplt jdbcTmplt=new JdbcTmpltManager(this.dataSource);
		return jdbcTmplt;
	}
}
