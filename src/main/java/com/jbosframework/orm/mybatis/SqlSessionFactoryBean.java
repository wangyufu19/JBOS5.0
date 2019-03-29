package com.jbosframework.orm.mybatis;
import java.io.IOException;
import java.util.Map;
import javax.sql.DataSource;
import com.jbosframework.context.ApplicationContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
/**
 * SqlSessionFactoryBean
 * @author youfu.wang
 * @version 1.0
 * @date 2019-01-07
 */
public class SqlSessionFactoryBean {
	public static final String sqlSessionFactoryBean="sqlSessionFactoryBean";
	private String id;
	private String packageName;
	private ApplicationContext ctx;
	private DataSource dataSource;
	private SqlSessionFactory sqlSessionFactory;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 构建SqlSessionFactory
	 * @return
	 * @throws IOException
	 */
	public SqlSessionFactory build(Map<String,String> contextProperties) {
		this.id=contextProperties.get("mybatis.environment.id");
		this.packageName=contextProperties.get("mybatis.packageName");
		this.sqlSessionFactory=SqlSessionFactoryHolder.build(this.getId(),this.getDataSource(),this.getPackageName());
		return this.sqlSessionFactory;
	}
	public Configuration getConfiguration(){
		if(this.sqlSessionFactory!=null){
			return this.sqlSessionFactory.getConfiguration();
		}
		return null;
	}
}
