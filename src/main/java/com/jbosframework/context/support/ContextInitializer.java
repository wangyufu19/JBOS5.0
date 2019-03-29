package com.jbosframework.context.support;
import com.jbosframework.boot.autoconfig.DataSourceConfig;
import com.jbosframework.context.Environment;
import com.jbosframework.context.support.ApplicationResourceContext;
import com.jbosframework.orm.mybatis.SqlSessionFactoryBean;

/**
 * ContextInitializer
 * @author youfu.wang
 * @version 1.0
 */
public class ContextInitializer {
    private ApplicationResourceContext applicationResourceContext=new ApplicationResourceContext();
    private Environment environment=new Environment();
    private boolean enableAspectJAutoProxy=false;

    /**
     * 构造方法
     */
    public ContextInitializer(){
        this.init();
    }

    /**
     * 初始化资源
     */
    private void init(){
        applicationResourceContext.init();
        DataSourceConfig dataSourceConfig=new DataSourceConfig(this);
        this.putBean(DataSourceConfig.dataSourceConfigBean,dataSourceConfig);
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean(this);
        this.putBean(SqlSessionFactoryBean.sqlSessionFactoryBean,sqlSessionFactoryBean.build());
    }
    /**
     * 得到上下文属性
     * @param name
     * @return
     */
    public String getContextProperty(String name){
        return applicationResourceContext.getContextProperty(name);
    }
    /**
     * 设置上下文环境
     * @return
     */
    public void setEnvironment(Environment environment){
        this.environment=environment;
    }
    /**
     * 得到上下文环境
     * @return
     */
    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnableAspectJAutoProxy(boolean enableAspectJAutoProxy){
        this.enableAspectJAutoProxy=enableAspectJAutoProxy;
    }
    public boolean getEnableAspectJAutoProxy(){
        return this.enableAspectJAutoProxy;
    }
}
