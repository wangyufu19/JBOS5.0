package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.annotation.EnableTransactionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TransactionAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableTransactionManager
@ConditionalOnBean(DataSourceTransactionManager.class)
public class TransactionAutoConfiguration extends AbstractAutoConfiguration {

    private static final Log log= LogFactory.getLog(DataSourceAutoConfiguration.class);

    /**
     * 注册自动配置组件到容器中
     * @return
     */
    public void registry() {
        Configuration configuration = this.getClass().getAnnotation(Configuration.class);
        if (configuration == null) {
            return;
        }
        EnableTransactionManager enableTransactionManager=this.getClass().getAnnotation(EnableTransactionManager.class);
        if(enableTransactionManager==null){
            return;
        }
        if(!this.conditionalOnBean(this.getClass().getAnnotation(ConditionalOnBean.class))){
            this.getApplicationContext().addBeanPostProcessor(new TransactionAdviceProcessor(this.getApplicationContext()));
            DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager();
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(DataSourceTransactionManager.class.getName(),DataSourceTransactionManager.class);
            this.getApplicationContext().putBeanDefinition(annotationBean);
            this.getApplicationContext().putBean(DataSourceTransactionManager.class.getName(),dataSourceTransactionManager);
        }
    }
}
