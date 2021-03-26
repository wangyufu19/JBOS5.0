package com.jbosframework.boot.autoconfig.async;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.support.BeanProcessorRegistry;
import com.jbosframework.schedule.AsyncBeanProcessor;
import com.jbosframework.schedule.annotation.AsyncConfiguration;
import com.jbosframework.schedule.annotation.EnableAsync;

/**
 * AsyncAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableAsync
public class AsyncAutoConfiguration extends AbstractAutoConfiguration {
    /**
     * 注册自动配置组件到容器中
     * @return
     */
    public void registry(){
        if (!this.conditionalOnConfiguration(this.getClass())) {
            return;
        }
        //开启异步处理
        EnableAsync enableAsync=this.getClass().getAnnotation(EnableAsync.class);
        if(enableAsync!=null){
            BeanProcessorRegistry beanProcessorRegistry=new AsyncConfiguration();
            beanProcessorRegistry.setBeanFactory(this.getApplicationContext());
            beanProcessorRegistry.registryBeanProcessor();
        }
    }
}
