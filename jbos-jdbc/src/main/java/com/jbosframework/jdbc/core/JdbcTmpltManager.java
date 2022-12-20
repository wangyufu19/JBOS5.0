package com.jbosframework.jdbc.core;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jbosframework.jdbc.datasource.DataSourceUtils;
import com.jbosframework.jdbc.support.JdbcUtils;
import com.jbosframework.jdbc.support.SqlAutowireFactory;
import com.jbosframework.jdbc.object.RowObjectFactory;
import com.jbosframework.cache.provider.CacheProvider;
import com.jbosframework.utils.TypeConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;

/**
 * JDBC模板管理类
 * 
 * @author youfu.wang
 * @version 1.0
 */
public class JdbcTmpltManager implements JdbcTmplt {
	/**
	 * ORACLE数据库
	 */
	public final static String DB_ORALCE="oracle";
	/**
	 * MYSQL数据库
	 */
	public final static String DB_MYSQL="mysql";
	/**
	 * MYSQL数据库
	 */
	public final static String DB_MSSQL="mssql";
	private DataSource dataSource;
	private Connection connection=null;
	private PreparedStatement pstmt=null;
	private CallableStatement cstmt=null;
	private String useCache="false";
	private CacheProvider cacheProvider;
	private Log log=LogFactory.getLog(JdbcTmpltManager.class);
	private boolean showSql=true;
	private String dialect=JdbcTmpltManager.DB_MYSQL;
	/**
	 * 构造方法
	 */
	public JdbcTmpltManager(){
		
	}
	/**
	 * 构造方法
	 * @param dataSource
	 */
	public JdbcTmpltManager(DataSource dataSource) {
		this.dataSource = dataSource;
		try {
			this.connection=DataSourceUtils.getConnection(dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		try {
			this.connection=DataSourceUtils.getConnection(dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到使用缓存
	 * @return
	 */
	public String getUseCache() {
		return useCache;
	}
	/**
	 * 设置使用缓存
	 * @param useCache
	 */
	public void setUseCache(String useCache) {
		this.useCache = useCache;
	}
	/**
	 * 得到缓存提供者
	 * @return
	 */
	public CacheProvider getCacheProvider() {
		return cacheProvider;
	}
	/**
	 * 设置缓存提供者
	 * @param cacheProvider
	 */
	public void setCacheProvider(CacheProvider cacheProvider) {
		this.cacheProvider = cacheProvider;
	}
	/**
	 * 设置数据源连接
	 * @param connection
	 */
	public void setConnection(Connection connection){
		this.connection=connection;
	}
	/**
	 * 得到数据源连接
	 * @return
	 */
	public Connection getConnection(){
		return this.connection;
	}
	/**
	 * 释放语句块资源
	 */
	public void closeStatement(){
		JdbcUtils.closeStatement(pstmt);
		JdbcUtils.closeStatement(cstmt);
		pstmt=null;
		cstmt=null;
	}
	private boolean getShowSql(){
		return this.showSql;
	}
	public String getDialect(){
		return this.dialect;
	}
	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @throws SQLException 
	 */
	public void execute(String sql) throws SQLException{			
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}		
		pstmt = connection.prepareStatement(sql);
		pstmt.executeUpdate();	
		this.closeStatement();
	}

	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException 
	 */
	public void execute(String sql, Object[] args) throws SQLException {
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		this.execute(sql);
	}

	/**
	 * 执行给定的SQL语句,该语句可能为 INSERT、UPDATE 或 DELETE 语句
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException 
	 */
	public void execute(String sql, Map args) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		this.execute(sql);
	}
	/**
	 * 执行存储过程({?=call PROC_TEST()})
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void executeProcedure(String sql) throws SQLException{
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}		
		cstmt=connection.prepareCall(sql);
		cstmt.execute();	
		this.closeStatement();
	}
	/**
	 * 执行存储过程({?=call PROC_TEST(?....)})
	 * 
	 * @param sql
	 * @param args
	 * @throws SQLException
	 */
	public void executeProcedure(String sql,Map<String,Object> args) throws SQLException{	
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}		
		cstmt=connection.prepareCall(sql);
		if(args!=null){
			for(Map.Entry<String,Object> entry:args.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				cstmt.setObject(key,value);
			}		
		}
		cstmt.execute();	
		this.closeStatement();
	}
	/**
	 * 执行存储过程({?=call PROC_TEST(?....)})
	 * 
	 * @param sql
	 * @param arg1
	 * @param arg2
	 * @throws SQLException
	 */
	public Map<String,Object> executeProcedure(String sql,Map<String,Object> arg1,Map<String,String> arg2) throws SQLException{
		Map<String,Object> out=new HashMap<String,Object>();
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}		
		cstmt=connection.prepareCall(sql);
		//设置输入参数
		if(arg1!=null){
			for(Map.Entry<String,Object> entry:arg1.entrySet()){
				String key=entry.getKey();
				Object value=entry.getValue();
				cstmt.setObject(key,value);
			}		
		}
		//设置输出参数和类型
		if(arg2!=null){
			for(Map.Entry<String,String> entry:arg2.entrySet()){
				String key=entry.getKey();
				String value=String.valueOf(entry.getValue());
				if(TypeConverter.DATA_TYPE_INTEGER.equals(value)||TypeConverter.DATA_TYPE_INTEGER_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.INTEGER);
				}else if(TypeConverter.DATA_TYPE_LONG.equals(value)||TypeConverter.DATA_TYPE_LONG_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.LONGVARCHAR);
				}else if(TypeConverter.DATA_TYPE_FLOAT.equals(value)||TypeConverter.DATA_TYPE_FLOAT_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.FLOAT);
				}else if(TypeConverter.DATA_TYPE_DOUBLE.equals(value)||TypeConverter.DATA_TYPE_DOUBLE_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.DOUBLE);
				}else if(TypeConverter.DATA_TYPE_STRING.equals(value)||TypeConverter.DATA_TYPE_STRING_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.VARCHAR);
				}else if(TypeConverter.DATA_TYPE_DATE.equals(value)||TypeConverter.DATA_TYPE_DATE_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.DATE);
				}else if(TypeConverter.DATA_TYPE_TIMESTAMP.equals(value)||TypeConverter.DATA_TYPE_TIMESTAMP_ALIAS.equals(value)){
					cstmt.registerOutParameter(key,Types.TIMESTAMP);
				}				
			}		
		}
		cstmt.execute();
		//设置输出参数值
		if(arg2!=null){
			for(Map.Entry<String,String> entry:arg2.entrySet()){
				String key=entry.getKey();
				out.put(key, cstmt.getObject(key));				
			}
		}
		this.closeStatement();
		return out;
	}
	/**
	 * 得到最大行数
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public int getROWNUM(String sql) throws SQLException{
		int rownum = 0;		
		ResultSet rst=null;
		sql = "select count(1) from (" + sql + ") t1";
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		while (rst.next()) {
			rownum = rst.getInt(1);
			break;
		}
		JdbcUtils.closeResultSet(rst);
		return rownum;
	}
	/**
	 * 执行给定的SQL语句，该语句返回数据结果集
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet queryForResultSet(String sql) throws SQLException{
		ResultSet rst=null;
		if("true".equals(this.getShowSql())){
			log.info("******SQL: "+sql);
		}		
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		return rst;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public Object[] queryForArray(String sql) throws SQLException{			
		Object[] obj=null;		
		//判断缓存是否有数据
		if("true".equals(this.useCache)){		
			obj=(Object[])this.cacheProvider.getCacheClient().getData(sql);
			if(obj!=null){
				return obj;
			}
		}
		ResultSet rst=null;
		rst=this.queryForResultSet(sql);
		while (rst.next()) {
			obj=RowObjectFactory.getRowObject(rst);
			//将数据加入到缓存
			if("true".equals(this.useCache)){	
				this.cacheProvider.getCacheClient().setData(sql,obj);
			}			
			break;
		}				
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return obj;
	}
	
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForArray(String sql,RowMapper<T> rowMapper) throws SQLException{
		T t=null;
		//判断缓存是否有数据
		if("true".equals(this.useCache)){		
			t=(T)this.cacheProvider.getCacheClient().getData(sql);
			if(t!=null)
				return t;
		}
		ResultSet rst=null;	
		rst=this.queryForResultSet(sql);
		while (rst.next()) {
			t=rowMapper.mapRow(rst, rst.getRow());
			//将数据加入到缓存
			if("true".equals(this.useCache)){		
				this.cacheProvider.getCacheClient().setData(sql, t);
			}		
			break;
		}
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return t;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param requiredType 
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForArray(String sql,Class<T> requiredType) throws SQLException{
		T t=null;
		//判断缓存是否有数据
		if("true".equals(this.useCache)){		
			t=(T)this.cacheProvider.getCacheClient().getData(sql);
			if(t!=null)
				return t;
		}
		ResultSet rst=null;	
		rst=this.queryForResultSet(sql);
		while (rst.next()) {
			t=RowObjectFactory.getRowObject(rst, requiredType);
			//将数据加入到缓存
			if("true".equals(this.useCache)){		
				this.cacheProvider.getCacheClient().setData(sql, t);
			}		
			break;
		}
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return t;
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	public Object[] queryForArray(String sql, Object[] args) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql);
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql, Object[] args,RowMapper<T> rowMapper) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql, rowMapper);
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
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql, requiredType);
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException 
	 */
	public Object[] queryForArray(String sql, Map args) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql);
	}
	/**
	 * 执行给定的SQL语句，该语句返回单个对象
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForArray(String sql, Map args,RowMapper<T> rowMapper) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql, rowMapper);
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
	public <T> T queryForArray(String sql,Map args,Class<T> requiredType) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForArray(sql, requiredType);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException 
	 */
	public List queryForList(String sql) throws SQLException{	
		List list = new ArrayList();
		ResultSet rst=null;
		rst=this.queryForResultSet(sql);
		while (rst.next()) {
			list.add(RowObjectFactory.getRowObject(rst));
		}
		
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,RowMapper<T> rowMapper) throws SQLException{
		List<T> list = new ArrayList<T>();
		ResultSet rst=null;
		rst=this.queryForResultSet(sql);
		while (rst.next()) {			
			list.add(rowMapper.mapRow(rst, rst.getRow()));
		}		
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Class<T> requiredType) throws SQLException{
		List<T> list = new ArrayList<T>();
		ResultSet rst=null;
		rst=this.queryForResultSet(sql);
		while (rst.next()) {			
			list.add(RowObjectFactory.getRowObject(rst, requiredType));
		}
		JdbcUtils.closeResultSet(rst);		
		this.closeStatement();
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql, Object[] args) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql, Object[] args,RowMapper<T> rowMapper) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql, rowMapper);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Object[] args,Class<T> requiredType) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql, requiredType);
	}
	
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public List queryForList(String sql, Map args) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql, Map args,RowMapper<T> rowMapper) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql, rowMapper);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param requiredType
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForList(String sql,Map args,Class<T> requiredType) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		return this.queryForList(sql, requiredType);
	}
	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, PageParam pageParam) throws SQLException{
		// 是否采用分页查询数据
		if (pageParam.getMaxResult() > 0) {
			int rownum = this.getROWNUM(sql);
			pageParam.setMaxRowNum(rownum);
			if (rownum > 0) {
				if (rownum % pageParam.getPageSize() == 0) {
					pageParam.setMaxPage(rownum / pageParam.getPageSize());
				} else
					pageParam
							.setMaxPage(rownum / pageParam.getPageSize() + 1);
			} else
				pageParam.setMaxPage(1);
			
			// 分别组装oracle,mssql,mysql数据库方言的分页查询语句
			if ("oracle".equals(this.getDialect())) {
				sql = "select * from (select rownum r,t1.* from (" + sql
						+ ") t1 where rownum<=" + pageParam.getMaxResult()
						+ ") t2 where r>=" + pageParam.getFirstResult();
			} else if ("mssql".equals(this.getDialect())) {
				sql = "select * from (select row_number() over(order by "
						+ pageParam.getPrimary() + ") r,t1.* from (" + sql
						+ ") t1 ) t2 where r between "
						+ pageParam.getFirstResult() + " and "
						+ pageParam.getMaxResult();

			} else if ("mysql".equals(this.getDialect())) {
				sql = "select * from (select t1.* from (" + sql
				+ ") t1) t2 limit " + (pageParam.getFirstResult()-1) + ","
				+ pageParam.getMaxResult();
			} else if ("db2".equals(this.getDialect())) {

			}
		}
		List list=null;
		list=this.queryForList(sql);
		if(list==null){
			pageParam.setFirstResult(0);
			pageParam.setMaxResult(0);
		}else{
			if(pageParam.getCurrentPage()==1){					
				
				pageParam.setMaxResult(list.size());
			}else{
				pageParam.setMaxResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+list.size());	
			}
			if(list.size()==0){
				pageParam.setFirstResult(0);
			}else{
				pageParam.setFirstResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+1);
			}
		}
		return list;
	}

	/**
	 * 执行给定的SQL语句，该语句返回多个对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, Object[] args, PageParam pageParam) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		// 是否采用分页查询数据
		if (pageParam.getMaxResult() > 0) {
			int rownum = this.getROWNUM(sql);
			pageParam.setMaxRowNum(rownum);
			if (rownum > 0) {
				if (rownum % pageParam.getPageSize() == 0) {
					pageParam.setMaxPage(rownum / pageParam.getPageSize());
				} else
					pageParam
							.setMaxPage(rownum / pageParam.getPageSize() + 1);
			} else
				pageParam.setMaxPage(1);
			
			// 分别组装oracle,mssql,mysql数据库方言的分页查询语句
			if ("oracle".equals(this.getDialect())) {
				sql = "select * from (select rownum r,t1.* from (" + sql
						+ ") t1 where rownum<=" + pageParam.getMaxResult()
						+ ") t2 where r>=" + pageParam.getFirstResult();
			} else if ("mssql".equals(this.getDialect())) {
				sql = "select * from (select row_number() over(order by "
						+ pageParam.getPrimary() + ") r,t1.* from (" + sql
						+ ") t1 ) t2 where r between "
						+ pageParam.getFirstResult() + " and "
						+ pageParam.getMaxResult();

			} else if ("mysql".equals(this.getDialect())) {
				sql = "select * from (select t1.* from (" + sql
				+ ") t1) t2 limit " + (pageParam.getFirstResult()-1) + ","
				+ pageParam.getMaxResult();
			} else if ("db2".equals(this.getDialect())) {

			}		
		}
		List list=null;
		list=this.queryForList(sql);
		if(list==null){
			pageParam.setFirstResult(0);
			pageParam.setMaxResult(0);
			pageParam.setMaxRowNum(0);
		}else{
			if(pageParam.getCurrentPage()==1){					
				
				pageParam.setMaxResult(list.size());
			}else{
				pageParam.setMaxResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+list.size());	
			}
			if(list.size()==0){
				pageParam.setFirstResult(0);
			}else{
				pageParam.setFirstResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+1);
			}
			
		}
		return list;
	}
	/**
	 * 执行给定的SQL语句，该语句返回分面查询对象的列表
	 * 
	 * @param sql
	 * @param args
	 * @param pageParam
	 * @return
	 * @throws SQLException
	 */
	public List queryForPage(String sql, Map args, PageParam pageParam) throws SQLException{
		sql = SqlAutowireFactory.makeUp(sql, args, this.getDialect());
		// 是否采用分页查询数据
		if (pageParam.getMaxResult() > 0) {
			int rownum = this.getROWNUM(sql);
			pageParam.setMaxRowNum(rownum);
			if (rownum > 0) {
				if (rownum % pageParam.getPageSize() == 0) {
					pageParam.setMaxPage(rownum / pageParam.getPageSize());
				} else
					pageParam
							.setMaxPage(rownum / pageParam.getPageSize() + 1);
			} else
				pageParam.setMaxPage(1);
			
			// 分别组装oracle,mssql,mysql数据库方言的分页查询语句
			if ("oracle".equals(this.getDialect())) {
				sql = "select * from (select rownum r,t1.* from (" + sql
						+ ") t1 where rownum<=" + pageParam.getMaxResult()
						+ ") t2 where r>=" + pageParam.getFirstResult();
			} else if ("mssql".equals(this.getDialect())) {
				sql = "select * from (select row_number() over(order by "
						+ pageParam.getPrimary() + ") r,t1.* from (" + sql
						+ ") t1 ) t2 where r between "
						+ pageParam.getFirstResult() + " and "
						+ pageParam.getMaxResult();

			} else if ("mysql".equals(this.getDialect())) {
				sql = "select * from (select t1.* from (" + sql
				+ ") t1) t2 limit " + (pageParam.getFirstResult()-1) + ","
				+ pageParam.getMaxResult();
			} else if ("db2".equals(this.getDialect())) {

			}
		}
		List list=this.queryForList(sql);;
		if(list==null){
			pageParam.setFirstResult(0);
			pageParam.setMaxResult(0);
		}else{
			if(pageParam.getCurrentPage()==1){					
				
				pageParam.setMaxResult(list.size());
			}else{
				pageParam.setMaxResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+list.size());	
			}
			if(list.size()==0){
				pageParam.setFirstResult(0);
			}else{
				pageParam.setFirstResult((pageParam.getCurrentPage()-1)*pageParam.getPageSize()+1);
			}
		}
		return list;
	}
	/**
	 * 执行给定的SQL语句,该语句读取Clob数据
	 * @param sql
	 * @param col
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public String readClob(String sql,String col) throws SQLException{
		ResultSet rst=null;
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		while(rst.next()){
			 java.sql.Clob clob = (java.sql.Clob) rst.getClob(col); 
			 BufferedReader in = new BufferedReader(clob.getCharacterStream());
             StringWriter out = new StringWriter();
             int c;
             try {
				while((c = in.read()) != -1) 
				     out.write(c);
				out.close();
	            in.close();
			 } catch (IOException e) {				
				e.printStackTrace();
			 }             
             String str = out.getBuffer().toString();            
             return str;
		}
		JdbcUtils.closeResultSet(rst);
		this.closeStatement();
		return "";
	}
	/**
	 * 执行给定的SQL语句,该语句保存Clob数据
	 * @param sql
	 * @param col
	 * @param data
	 * @throws SQLException 
	 * @throws Exception
	 */
	public synchronized void executeClob(String sql,String col,String data) throws SQLException{
	
		ResultSet rst=null;
		if("true".equals(this.getShowSql())){
			log.info("SQL: "+sql);
		}
		connection.setAutoCommit(false);
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		
		while(rst.next()){					
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rst.getObject(col);   			
	        BufferedWriter out=null;	       
	        if(clob!=null){
	        	out= new BufferedWriter(clob.getCharacterOutputStream());	 	        		 	        
	        }	 	    	      
	        if(out!=null){
	        	try {
					out.write(data);
					out.flush();
					out.close();
				} catch (IOException e) {				
					e.printStackTrace();
				}	  	
	        }
		}		
		connection.commit();
		JdbcUtils.closeResultSet(rst);
		this.closeStatement();
	}
	/**
	 * 执行给定的SQL语句,该语句读取Blob数据
	 * @param sql
	 * @param col
	 * @return
	 * @throws SQLException 
	 * @throws Exception
	 */
	public OutputStream readBlob(String sql,String col) throws SQLException{
		ResultSet rst=null;		
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		OutputStream output=null;
		while(rst.next()){
			java.sql.Blob blob =rst.getBlob(col);
            if(blob != null){
                BufferedOutputStream out = new BufferedOutputStream(output);
                BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
                int c;
                try {
					while((c = in.read()) != -1) 
					    out.write(c);
					in.close();
	                out.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}                
            }
		}
		JdbcUtils.closeResultSet(rst);
		this.closeStatement();
		return output;
	}
	/**
	 * 执行给定的SQL语句,该语句保存Blob数据
	 * @param sql
	 * @param col
	 * @param data
	 * @throws SQLException 
	 * @throws Exception
	 */
	public synchronized void updateBlob(String sql,String col,String data) throws SQLException{
		ResultSet rst=null;
		pstmt = connection.prepareStatement(sql);
		rst = pstmt.executeQuery();
		while(rst.next()){
			oracle.sql.BLOB blob=(oracle.sql.BLOB)rst.getObject(col);  
			BufferedOutputStream out=null;
			if(blob!=null){
				out=new BufferedOutputStream(blob.getBinaryOutputStream());				
			}
			if(out!=null){
				byte[] buffer=data.getBytes();
				try {
					out.write(buffer);
					out.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}				
			}
		}
		JdbcUtils.closeResultSet(rst);
		this.closeStatement();
	}
}
