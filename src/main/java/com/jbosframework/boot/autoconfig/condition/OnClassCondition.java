package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Condition;
import com.jbosframework.core.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OnClassCondition
 * @author youfu.wang
 * @version 1.0
 */
public class OnClassCondition implements Condition {
    private static final Log log= LogFactory.getLog(OnClassCondition.class);

    public boolean matches(ApplicationContext ctx, Class<?>[] autoConfigurationClasses) {
        boolean bool=false;
        if (autoConfigurationClasses!=null){
            for(Class<?> autoConfigurationClass:autoConfigurationClasses){
                bool=JBOSClassloader.isPresent(autoConfigurationClasses.getClass().getName());
            }
        }
        return bool;
    }
}
