package com.application.examples.config;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@Slf4j
public class SqlSessionFactoryBeanConfig {
    @Value("${mybatis.environment.id}")
    private String id;
    @Value("${mybatis.packageName}")
    private String packageName;
    @Autowired
    private DataSource defaultDataSource;

    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactory getSqlSessionFactoryBean(){
//        log.info("******init getSqlSessionFactoryBean");
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(defaultDataSource);
        sqlSessionFactoryBean.setId(id);
        sqlSessionFactoryBean.setPackageName(packageName);
        return sqlSessionFactoryBean.build();
    }
}
