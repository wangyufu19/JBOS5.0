package com.jbosframework.orm.mybatis;
import java.io.IOException;
import javax.sql.DataSource;
import com.jbosframework.boot.autoconfig.DataSourceConfig;
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
	private DataSourceConfig dataSourceConfig;
	private DataSource dataSource;
	private SqlSessionFactory sqlSessionFactory;
	/**
	 * 构造方法
	 * @param ctx
	 */
	public SqlSessionFactoryBean(ApplicationContext ctx){
		this.ctx=ctx;
	}
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
	public SqlSessionFactory build() {
		this.id=ctx.getContextProperty("mybatis.environment.id");
		this.packageName=ctx.getContextProperty("mybatis.packageName");
		this.dataSourceConfig=ctx.getBean(DataSourceConfig.dataSourceConfigBean,DataSourceConfig.class);
		this.dataSource=this.dataSourceConfig.getDataSource();
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
