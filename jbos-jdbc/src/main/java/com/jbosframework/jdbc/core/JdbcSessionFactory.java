package com.jbosframework.jdbc.core;
import com.jbosframework.jdbc.core.JdbcSession;
import com.jbosframework.jdbc.datasource.JDBCDataSource;
import com.jbosframework.jdbc.core.JdbcSessionImpl;
/**
 * JDBC会话工厂类
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcSessionFactory{
	/**
	 * 创建JDBC会话
	 * @param dataSource
	 * @return
	 */
	public static JdbcSession createJdbcSession(JDBCDataSource dataSource){
		JdbcSession jdbcSession=new JdbcSessionImpl(dataSource);
		return jdbcSession;
	}
}
