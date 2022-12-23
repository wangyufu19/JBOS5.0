package com.jbosframework.schedule.annotation;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;
import java.util.List;

/**
 * ScheduleRegister
 * @author youfu.wang
 * @version 5.0
 */

public class ScheduleRegister implements ImportSelector {
    private ConfigurableApplicationContext applicationContext;

    public ScheduleRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        ScheduleBeanPostProcessor scheduleBeanPostProcessor=new ScheduleBeanPostProcessor(this.applicationContext);
        this.applicationContext.getBeanFactory().registerBeanPostProcessor(scheduleBeanPostProcessor);
        return null;
    }
}
