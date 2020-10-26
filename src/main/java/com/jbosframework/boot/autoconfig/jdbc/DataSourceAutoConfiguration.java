package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Configuration;
import javax.sql.DataSource;


/**
 * DataSourceAutoConfiguration
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration extends AbstractAutoConfiguration {
    /**
     * 注册自动配置组件到容器中
     * @param ctx
     * @return
     */
    public void registry(ApplicationContext ctx){
        Configuration configuration=this.getClass().getAnnotation(Configuration.class);
        if (configuration==null){
            return;
        }
        if(this.conditionalOnClass(this.getClass().getAnnotation(ConditionalOnClass.class))){
            Class<?>[] dataSourceClses=DataSourceConfiguration.class.getDeclaredClasses();
            if(dataSourceClses!=null){
                for(Class<?> dataSourceCls:dataSourceClses){
                    if(this.conditionalOnClass(dataSourceCls.getAnnotation(ConditionalOnClass.class))&&this.conditionalOnProperty(dataSourceCls.getAnnotation(ConditionalOnProperty.class))){
                      this.doCreateDataSource(ctx,dataSourceCls);
                        break;
                    }
                }
            }
        }
    }
    private void doCreateDataSource(ApplicationContext ctx,Class<?> dataSourceCls){
        ConfigurationProperties configurationPropertiesAnnotation=dataSourceCls.getAnnotation(ConfigurationProperties.class);
        if (configurationPropertiesAnnotation!=null){
            Object properties=ctx.getContextConfiguration().getContextProperty(configurationPropertiesAnnotation.prefix());
            DataSourceProperties dataSourceProperties=new DataSourceProperties(properties);
        }
    }
}
