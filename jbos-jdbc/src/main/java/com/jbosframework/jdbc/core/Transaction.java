package com.jbosframework.jdbc.core;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * JDBC事务接口
 * @author youfu.wang
 * @version 1.0
 */
public interface Transaction {
	/**
	 * 设置数据源连接
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource);
	/**
	 * 得到数据源连接
	 * @return
	 */
	public DataSource getDataSource();
	/**
	 * 开始事务
	 * @throws SQLException 
	 */
	public void begin() throws SQLException;
	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public void commit() throws SQLException;
	/**
	 * 回滚事务
	 * @throws SQLException
	 */
	public void rollback() throws SQLException;
	/**
	 * 事务是否已经提交
	 * @return
	 */
	public boolean wasCommitted();
	/**
	 * 事务是否已经回滚
	 * @return
	 */
	public boolean wasRolledBack();
}
