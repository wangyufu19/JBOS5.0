package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.core.JBOSClassCaller;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.sql.DataSource;


/**
 * DataSourceAutoConfiguration
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@ConditionalOnClass(DataSource.class)
public class DataSourceAutoConfiguration extends AbstractAutoConfiguration {
    private static final Log log= LogFactory.getLog(DataSourceAutoConfiguration.class);
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
                    if(this.conditionalOnClass(dataSourceCls.getAnnotation(ConditionalOnClass.class))&&
                       this.conditionalOnBean(dataSourceCls.getAnnotation(ConditionalOnBean.class))&&
                       this.conditionalOnProperty(dataSourceCls.getAnnotation(ConditionalOnProperty.class))){
                        //自动配置数据源
                        this.doCreateDataSource(ctx,dataSourceCls);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 自动配置数据源
     * @param ctx
     * @param dataSourceCls
     */
    private void doCreateDataSource(ApplicationContext ctx,Class<?> dataSourceCls){
        Object obj=null;
        ConfigurationProperties configurationPropertiesAnnotation=dataSourceCls.getAnnotation(ConfigurationProperties.class);
        if (configurationPropertiesAnnotation!=null){
            DataSourceProperties dataSourceProperties=null;
            String type= StringUtils.replaceNull(ctx.getContextConfiguration().getContextProperty(DataSourceProperties.DATASOURCE_TYPE));
            String driverClass=StringUtils.replaceNull(ctx.getContextConfiguration().getContextProperty(DataSourceProperties.DATASOURCE_DRIVERCLASS));
            Object properties=ctx.getContextConfiguration().getContextProperty(configurationPropertiesAnnotation.prefix());
            if (DataSourceProperties.DATASOURCE_TYPE_TOMCAT.equals(type)){
                dataSourceProperties=new TomcatDataSourceProperties();
            }
            if(dataSourceProperties!=null){
                dataSourceProperties.setType(type);
                dataSourceProperties.setDriverClass(driverClass);
                dataSourceProperties.load(properties);
            }
            Object[] parameterValues=new Object[1];
            parameterValues[0]=dataSourceProperties;
            Class<?>[] parameterTypes=new Class<?>[1];
            parameterTypes[0]=DataSourceProperties.class;
            obj=JBOSClassCaller.call(dataSourceCls,"getDataSource",parameterValues,parameterTypes);
            if(obj!=null){
                DataSource dataSource=(DataSource)obj;
                log.info("******dataSource "+dataSource);
                ctx.putBean(dataSource.getClass().getName(),dataSource);
            }
        }
    }
}
