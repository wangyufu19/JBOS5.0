package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(DataSource.class)
public class Tomcat {
    public Tomcat(){

    }
    @Bean
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }
}
