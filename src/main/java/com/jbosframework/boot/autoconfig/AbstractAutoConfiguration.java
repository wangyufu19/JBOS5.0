package com.jbosframework.boot.autoconfig;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Configuration;

/**
 * AutoConfiguration
 * @author youfu.wang
 * @version 1.0
 */

public abstract class AbstractAutoConfiguration {

    /**
     * 注册自动配置组件到容器中
     * @param ctx
     * @return
     */
    public abstract void registry(ApplicationContext ctx);

    /**
     * 捕获注解条件类
     * @param conditionOnClass
     */
    public boolean conditionalOnClass(ConditionalOnClass conditionOnClass){
        boolean bool=false;


        return bool;
    }

    /**
     * 捕获注解属性条件类
     * @param conditionalOnProperty
     * @return
     */
    public boolean conditionalOnProperty(ConditionalOnProperty conditionalOnProperty){
        boolean bool=false;


        return bool;
    }

}
