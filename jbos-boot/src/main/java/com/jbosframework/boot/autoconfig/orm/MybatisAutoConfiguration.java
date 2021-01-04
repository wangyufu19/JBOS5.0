package com.jbosframework.boot.autoconfig.orm;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.orm.mybatis.support.AnnotationMapperProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MybatisAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableMapperScan
@ConditionalOnClass(SqlSessionFactory.class)
public class MybatisAutoConfiguration extends AbstractAutoConfiguration {

    private static final Log log= LogFactory.getLog(MybatisAutoConfiguration.class);

    public void registry() {
        Configuration configuration = this.getClass().getAnnotation(Configuration.class);
        if (configuration == null) {
            return;
        }

        EnableMapperScan enableMapperScan=this.getClass().getAnnotation(EnableMapperScan.class);
        if(enableMapperScan==null){
            return;
        }
        if(this.conditionalOnClass(this.getClass().getAnnotation(ConditionalOnClass.class))){
            this.getApplicationContext().addBeanPostProcessor(new AnnotationMapperProcessor(this.getApplicationContext()));
        }
    }
}
