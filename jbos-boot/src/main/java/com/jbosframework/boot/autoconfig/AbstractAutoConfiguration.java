package com.jbosframework.boot.autoconfig;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Condition;
import com.jbosframework.context.annotation.Conditional;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;

/**
 * AutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */

public abstract class AbstractAutoConfiguration {
    private static final Log log= LogFactory.getLog(AbstractAutoConfiguration.class);
    private ApplicationContext ctx;

    /**
     * 注册自动配置组件到容器中
     * @return
     */
    public abstract void registry();

    public void setApplicationContext(ApplicationContext ctx){
        this.ctx=ctx;
    }
    public ApplicationContext getApplicationContext(){
        return this.ctx;
    }

    public boolean conditionalOnConfiguration(Class<?> cls ){
        Configuration configuration = cls.getAnnotation(Configuration.class);
        if (configuration == null) {
            return false;
        }
        return true;
    }
    /**
     * 捕获注解条件类
     * @param conditionOnClass
     */
    public boolean conditionalOnClass(ConditionalOnClass conditionOnClass){
        if (conditionOnClass!=null){
            Class<?>[] autoConfigurationClasses=conditionOnClass.value();
            Conditional conditionalAnnotation=ConditionalOnClass.class.getDeclaredAnnotation(Conditional.class);
            if(conditionalAnnotation!=null){
                Condition condition=(Condition)JBOSClassloader.newInstance(conditionalAnnotation.value());
                return condition.matches(this.ctx,autoConfigurationClasses);
            }
        }
        return false;
    }
    /**
     * 捕获注解条件Bean类
     * @param conditionalOnBean
     */
    public boolean conditionalOnBean(ConditionalOnBean conditionalOnBean){
        if (conditionalOnBean!=null){
            Class<?>[] autoConfigurationClasses=conditionalOnBean.value();
            Conditional conditionalAnnotation=ConditionalOnBean.class.getDeclaredAnnotation(Conditional.class);
            if(conditionalAnnotation!=null){
                Condition condition=(Condition)JBOSClassloader.newInstance(conditionalAnnotation.value());
                return condition.matches(this.ctx,autoConfigurationClasses);
            }
        }
        return false;
    }
    /**
     * 捕获注解属性条件类
     * @param conditionalOnProperty
     * @return
     */
    public boolean conditionalOnProperty(ConditionalOnProperty conditionalOnProperty){
        if(conditionalOnProperty!=null){
            String name=StringUtils.replaceNull(conditionalOnProperty.name());
            String value=StringUtils.replaceNull(conditionalOnProperty.value());
            if(StringUtils.replaceNull(ctx.getContextConfiguration().getContextProperty(name)).equals(value)){
                return true;
            }
        }
        return false;
    }

}
