package com.jbosframework.jdbc.core;

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
