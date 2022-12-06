//package com.jbosframework.boot.autoconfig.transaction;
//
//import com.jbosframework.beans.config.GenericBeanDefinition;
//import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
//import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
//import com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration;
//import com.jbosframework.context.annotation.Configuration;
//import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import javax.sql.DataSource;
//
///**
// * TransactionAutoConfiguration
// * @author youfu.wang
// * @version 5.0
// */
//@Configuration
//@ConditionalOnBean(DataSourceTransactionManager.class)
//public class TransactionAutoConfiguration extends AbstractAutoConfiguration {
//
//    private static final Log log= LogFactory.getLog(DataSourceAutoConfiguration.class);
//
//    /**
//     * 注册自动配置组件到容器中
//     * @return
//     */
//    public void registry() {
//        if (!this.conditionalOnConfiguration(this.getClass())) {
//            return;
//        }
//        if(!this.conditionalOnBean(this.getClass().getAnnotation(ConditionalOnBean.class))){
//            DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager(this.getApplicationContext().getBean(DataSource.class));
//            GenericBeanDefinition annotationBean=new GenericBeanDefinition(DataSourceTransactionManager.class);
//            this.getApplicationContext().putBeanDefinition(annotationBean.getName(),annotationBean);
//            this.getApplicationContext().registerSingletonInstance(DataSourceTransactionManager.class.getName(),dataSourceTransactionManager);
//        }
//    }
//}
