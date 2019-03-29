package com.jbosframework.boot.autoconfig;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.jdbc.datasource.DriverManagerDataSource;
import com.jbosframework.jdbc.datasource.JndiNamingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */

public class DataSourceConfig {
	public static final Log log= LogFactory.getLog(DataSourceConfig.class);

	public static final String dataSourceConfigBean="dataSourceConfigBean";

	public static final String DATASOURCE_TYPE_JDBC="jdbc";

	public static final String DATASOURCE_TYPE_JNDI="jndi";

	private String type;

	private String driverClass;

	private String url;

	private String username;

	private String password;

	private String jndiName;

	private ApplicationContext ctx;

	/**
	 * 构造方法
	 * @param ctx
	 */
	public DataSourceConfig(ApplicationContext ctx){
		this.ctx=ctx;
		this.init();
	}
	private void init(){
		this.type=ctx.getContextProperty("jbos.datasource.type");
		this.driverClass=ctx.getContextProperty("jbos.datasource.driverClass");
		this.url=ctx.getContextProperty("jbos.datasource.url");
		this.username=ctx.getContextProperty("jbos.datasource.username");
		this.password=ctx.getContextProperty("jbos.datasource.password");
		this.jndiName=ctx.getContextProperty("jbos.datasource.jndiName");
	}
	/**
	 * 得到数据源对象
	 * @return
	 */
	public DataSource getDataSource() {
		if(DataSourceConfig.DATASOURCE_TYPE_JDBC.equals(type)){
			DriverManagerDataSource dataSource=new DriverManagerDataSource();
			dataSource.setDriverClass(driverClass);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		}else if(DataSourceConfig.DATASOURCE_TYPE_JNDI.equals(type)){
			JndiNamingDataSource dataSource=new JndiNamingDataSource();
			dataSource.setJndiName(jndiName);
			return  dataSource;
		}
		return null;
	}
}
