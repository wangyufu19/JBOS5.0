package com.application.examples.config;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@Slf4j
public class SqlSessionFactoryBeanConfig {
    @Value("${jbos.mybatis.mapperLocations}")
    private String mapperLocations;
    @Autowired
    private DataSource defaultDataSource;

    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactory getSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(defaultDataSource);
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        return sqlSessionFactoryBean.build();
    }
}
