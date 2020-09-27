package com.application.examples.config;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.jdbc.datasource.DriverManagerDataSource;
import com.jbosframework.jdbc.datasource.JndiNamingDataSource;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@Slf4j
public class DataSourceConfig {

	public static final String DATASOURCE_TYPE_JDBC="jdbc";

	public static final String DATASOURCE_TYPE_JNDI="jndi";

	@Value("${jbos.datasource.type}")
	private String type;
	@Value("${jbos.datasource.driverClass}")
	private String driverClass;
	@Value("${jbos.datasource.url}")
	private String url;
	@Value("${jbos.datasource.username}")
	private String username;
	@Value("${jbos.datasource.password}")
	private String password;
	@Value("${jbos.datasource.jndiName}")
	private String jndiName;

	/**
	 * 得到数据源对象
	 * @return
	 */
	@Bean("defaultDataSource")
	public DataSource getDataSource() {
		log.info("******init datasource");
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
