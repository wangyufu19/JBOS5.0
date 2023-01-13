package com.jbosframework.boot.autoconfig.orm.mybatis;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnMissingBean;
import com.jbosframework.boot.context.EnableConfigurationProperties;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.Import;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;

/**
 * MybatisAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MyBatisProperties.class)
@Import(MybatisMapperRegister.class)
public class MybatisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(SqlSessionFactory.class)
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource,MyBatisProperties properties){
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(properties.getMapperLocations());
        sqlSessionFactoryBean.setLogImpl(properties.getLogImpl());
        return sqlSessionFactoryBean.build();
    }
}
