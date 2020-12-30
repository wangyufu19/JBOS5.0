package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
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

    }
}
