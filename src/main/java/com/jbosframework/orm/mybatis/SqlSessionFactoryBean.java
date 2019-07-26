package com.jbosframework.orm.mybatis;
import java.io.IOException;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

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
	private DataSource dataSource;
	private static SqlSessionFactory sqlSessionFactory=null;

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
		if(sqlSessionFactory==null){
			synchronized (SqlSessionFactory.class) {
				if(sqlSessionFactory==null){
					TransactionFactory transactionFactory = new JdbcTransactionFactory();
					Environment environment = new Environment(id, transactionFactory, dataSource);
					Configuration configuration = new Configuration(environment);
					configuration.addMappers(packageName);
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
				}
			}
		}
		return this.sqlSessionFactory;
	}
	public Configuration getConfiguration(){
		if(this.sqlSessionFactory!=null){
			return this.sqlSessionFactory.getConfiguration();
		}
		return null;
	}
}
