package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnMissingBean;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.Import;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.sql.DataSource;


/**
 * DataSourceAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@Import(DataSourcePropertiesRegister.class)
public class DataSourceAutoConfiguration{
    private static final Log logger= LogFactory.getLog(DataSourceAutoConfiguration.class);

    @Configuration
    @ConditionalOnMissingBean(DataSource.class)
    @Import({Tomcat.class})
    protected static class DataSourceConnectionPool{
        DataSourceConnectionPool(){

        }
    }

}
