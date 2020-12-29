package com.jbosframework.jdbc.core;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * JDBC模板接口
 * 
 * @author youfu.wang
 * @version 1.0
 */
public interface JdbcTmplt {
	/**
	 * 设置数据源对象
	 */
	public void setDataSource(DataSource dataSource);
	/**
	 * 得到数据源对象
	 * @return
	 */
	public DataSource getDataSource();
	/**
	 * 设置数据源连接
	 * @param Connection
	 */
	public void setConnection(Connection Connection);
	/**
	 * 得到数据源连接
	 * @return
	 */
	public Connection getConnection();
	/**
	 * 释放语句块资源
	 */
	public void closeStatement();
	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void execute(String sql) throws SQLException;

	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException
	 */
	public void execute(String sql, Object[] args) throws SQLException;

	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException
	 */
	public void execute(String sql, Map args) throws SQLException;

	/**
	 * 执行存储过程
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void executeProcedure(String sql) throws SQLException;
	/**
	 * 执行存储过程
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException
	 */
	public void executeProcedure(String sql,Map<String,Object> args) throws SQLException;
	/**
	 * 执行存储过程
	 * 
	 * @param sql
     * @param args
	 * @param args1
	 * @throws SQLException
	 */
	public Map<String,Object> executeProcedure(String sql,Map<String,Object> args,Map<String,String> args1) throws SQLException;
	/**
	 * 得到最大行数
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int getROWNUM(String sql) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Object[] queryForArray(String sql) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param rowMapper 
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param requiredType 
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public Object[] queryForArray(String sql, Object[] args) throws SQLException;

	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql, Object[] args,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,Object[] args,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public Object[] queryForArray(String sql, Map args) throws SQLException;
	
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql, Map args,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,Map args,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql) throws SQLException;
	
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql, Object[] args) throws SQLException;

	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql, Object[] args,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Object[] args,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql, Map args) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql, Map args,RowMapper<T> rowMapper) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Map args,Class<T> requiredType) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回分面查询对象的列表
	 * 
	 * @param sql
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, PageParam pageParam) throws SQLException;

	/**
	 * 执行给定的SQL语句，该语句返回分面查询对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, Object[] args, PageParam pageParam) throws SQLException;
	/**
	 * 执行给定的SQL语句，该语句返回分面查询对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, Map args, PageParam pageParam) throws SQLException;
	/**
	 * 执行给定的SQL语句,该语句保存Clob数据
	 * @param sql
	 * @param col
	 * @param data
	 * @throws SQLException 
	 * @throws Exception
	 */
	public void executeClob(String sql,String col,String data) throws SQLException;
	/**
	 * 执行给定的SQL语句,该语句读取Clob数据
	 * @param sql
	 * @param col
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public String readClob(String sql,String col) throws SQLException;
}
