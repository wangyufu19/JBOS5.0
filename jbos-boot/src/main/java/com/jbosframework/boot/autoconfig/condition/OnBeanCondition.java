package com.jbosframework.boot.autoconfig.condition;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Condition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OnBeanCondition
 * @author youfu.wang
 * @version 1.0
 */
public class OnBeanCondition implements Condition {
    private static final Log log= LogFactory.getLog(OnBeanCondition.class);

    public boolean matches(ApplicationContext ctx, Class<?>[] autoConfigurationClasses) {
        boolean bool=false;
        if(autoConfigurationClasses!=null){
            for(Class<?> autoConfigurationClass:autoConfigurationClasses){
                bool= ctx.containsBean(autoConfigurationClass.getName());
            }
        }
        return bool;
    }
}
