package com.jbosframework.jdbc.support;
import java.sql.SQLException;
import java.util.List;
import com.jbosframework.jdbc.core.JdbcSessionFactory;
import com.jbosframework.jdbc.core.JdbcTmplt;
import com.jbosframework.jdbc.core.PageParam;

/**
 * JDBC DAO支撑类
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcDaoSupport {
	private JDBCDataSource dataSource;
	private JdbcTmplt jdbcTmplt;
	
	public JdbcDaoSupport(){
		
	}
	public JdbcDaoSupport(JDBCDataSource dataSource) {
		this.dataSource=dataSource;		
		this.jdbcTmplt=JdbcSessionFactory.createJdbcSession(dataSource).getJdbcTmplt();
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
		this.jdbcTmplt=JdbcSessionFactory.createJdbcSession(dataSource).getJdbcTmplt();
	}
	/**
	 * 得到JdbcTmplt
	 * @return
	 */
	public JdbcTmplt getJdbcTmplt() {
		return jdbcTmplt;
	}

	/**
	 * 执行给定的SQL语句
	 * @param sql
	 * @throws SQLException 
	 */
	public void execute(String sql) throws SQLException{		
		jdbcTmplt.execute(sql);				
	}
	/**
	 * 执行给定的SQL语句
	 * @param sql
	 * @param args
	 * @throws SQLException 
	 */
	public void execute(String sql,Object[] args) throws SQLException{		
		jdbcTmplt.execute(sql,args);					
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个Object对象
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public Object[] queryForArray(String sql) throws SQLException{
		Object[] obj=null;		
		obj = jdbcTmplt.queryForArray(sql);			
		return obj;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param requiredType 
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,Class<T> requiredType) throws SQLException{
		T t=null;
		t=jdbcTmplt.queryForArray(sql, requiredType);
		return t;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个Object对象
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public Object[] queryForArray(String sql, Object[] args) throws SQLException{		
		Object[] obj=null;		
		obj = jdbcTmplt.queryForArray(sql, args);				
		return obj;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql,Object[] args,Class<T> requiredType) throws SQLException{
		T t=null;
		t=jdbcTmplt.queryForArray(sql,args,requiredType);
		return t;
	}
	
	/**
	 * 执行给定的SQL语句，该语句返回List列表
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql) throws SQLException{
		List list=null;		
		list = jdbcTmplt.queryForList(sql);				
		return list;
	}

	/**
	 * 执行给定的SQL语句，该语句返回List列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql, Object[] args) throws SQLException{
		List list=null;
		list = jdbcTmplt.queryForList(sql, args);		
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回分页List列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql,PageParam pageParam) throws SQLException{
		List list=null;
		list = jdbcTmplt.queryForPage(sql, pageParam);	
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回分页List列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql,Object[] args,PageParam pageParam) throws SQLException{
		List list=null;
		list = jdbcTmplt.queryForPage(sql,args,pageParam);	
		return list;
	}
}
