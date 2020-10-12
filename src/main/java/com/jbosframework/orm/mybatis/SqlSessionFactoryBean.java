package com.jbosframework.orm.mybatis;
import javax.sql.DataSource;

import com.jbosframework.core.io.PathMatchResourceSupport;
import com.jbosframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import java.io.IOException;


/**
 * SqlSessionFactoryBean
 * @author youfu.wang
 * @version 1.0
 * @date 2019-01-07
 */
@Slf4j
public class SqlSessionFactoryBean {
	//development和work
	private String id="work";
	private String mapperLocations;
	private DataSource dataSource;
	private static SqlSessionFactory sqlSessionFactory=null;

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getMapperLocations() {
		return mapperLocations;
	}
	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	/**
	 * 构建SqlSessionFactory
	 * @return
	 */
	public SqlSessionFactory build() throws IOException {
		if(sqlSessionFactory==null){
			synchronized (SqlSessionFactory.class) {
				if(sqlSessionFactory==null){
					TransactionFactory transactionFactory = new JdbcTransactionFactory();
					Environment environment = new Environment(id, transactionFactory, dataSource);
					Configuration configuration = new Configuration(environment);
					PathMatchResourceSupport pathMatchResourceSupport=new PathMatchResourceSupport();
					Resource[] resources=pathMatchResourceSupport.getResources(mapperLocations);
					if(resources!=null){
						for(Resource resource:resources){
							XMLMapperBuilder mapperParser = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.getFileName(), configuration.getSqlFragments());
							mapperParser.parse();
						}
					}
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
				}
			}
		}
		return sqlSessionFactory;
	}
	public Configuration getConfiguration(){
		if(sqlSessionFactory!=null){
			return sqlSessionFactory.getConfiguration();
		}
		return null;
	}
}
