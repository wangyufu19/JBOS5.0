package com.jbosframework.boot.autoconfig.schedule;

import com.jbosframework.boot.autoconfig.AbstractAutoConfiguration;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.support.BeanProcessorRegistry;
import com.jbosframework.schedule.annotation.EnableScheduling;
import com.jbosframework.schedule.annotation.ScheduleConfiguration;
import org.quartz.Scheduler;

/**
 * ScheduleAutoConfiguration
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
@EnableScheduling
@ConditionalOnBean(Scheduler.class)
public class ScheduleAutoConfiguration extends AbstractAutoConfiguration {
      /**
     * 注册自动配置组件到容器中
     * @return
     */
    public void registry(){
        if (!this.conditionalOnConfiguration(this.getClass())) {
            return;
        }
        EnableScheduling enableScheduling=this.getClass().getDeclaredAnnotation(EnableScheduling.class);
        if(enableScheduling!=null&&!this.conditionalOnBean(this.getClass().getAnnotation(ConditionalOnBean.class))){
            BeanProcessorRegistry beanProcessorRegistry=new ScheduleConfiguration();
            beanProcessorRegistry.setBeanFactory(this.getApplicationContext());
            beanProcessorRegistry.registryBeanProcessor();
        }
    }
}
