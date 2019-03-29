package com.jbosframework.jdbc.core;
import java.sql.SQLException;
import com.jbosframework.jdbc.core.JdbcSession;
import com.jbosframework.jdbc.core.JdbcTmpltManager;
import com.jbosframework.jdbc.core.Transaction;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.jdbc.datasource.JDBCDataSource;
/**
 * JDBC会话实现类
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcSessionImpl implements JdbcSession{
	private JDBCDataSource dataSource;
	/**
	 * 构造方法
	 */
	public JdbcSessionImpl(){
		
	}
	/**
	 * 构造方法
	 * @param dataSource
	 */
	public JdbcSessionImpl(JDBCDataSource dataSource){
		this.dataSource=dataSource;
	}
	/**
	 * 得到数据源对象
	 * @return
	 */
	public JDBCDataSource getDataSource(){
		return this.dataSource;
	}
	/**
	 * 设置数据源对象
	 */
	public void setDataSource(JDBCDataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 开始一个事务
	 * @throws SQLException 
	 */
	public Transaction beginTransaction() throws SQLException{
		Transaction tx=new DataSourceTransactionManager(this.dataSource);
		tx.begin();
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
