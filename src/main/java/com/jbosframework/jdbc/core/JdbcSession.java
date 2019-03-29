package com.jbosframework.jdbc.core;
import java.sql.SQLException;
import com.jbosframework.jdbc.core.JdbcTmplt;
import com.jbosframework.jdbc.datasource.JDBCDataSource;
/**
 * JDBC会话接口类
 * @author youfu.wang
 * @version 1.0
 */
public interface JdbcSession {
	
	/**
	 * 设置数据源对象
	 */
	public void setDataSource(JDBCDataSource dataSource);
	/**
	 * 得到数据源对象
	 * @return
	 */
	public JDBCDataSource getDataSource();
	/**
	 * 开始一个事务
	 */
	public Transaction beginTransaction() throws SQLException;
	/**
	 * 得到JdbcTmplt
	 * @return
	 */
	public JdbcTmplt getJdbcTmplt();

}
