package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;
import com.jbosframework.utils.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OnClassCondition
 * @author youfu.wang
 * @version 1.0
 */
public class OnClassCondition extends AbstractCondition {
    private static final Log logger= LogFactory.getLog(OnClassCondition.class);
    private Class<?>[] classes;

    public OnClassCondition(ConfigurableApplicationContext ctx, Class<?>[] classes){
        super(ctx);
        this.classes=classes;
    }
    public boolean matches() {
        boolean bool=false;
        if (classes!=null){
            for(Class<?> cls:classes){
                bool=JBOSClassloader.isPresent(cls.getName());
            }
        }
        return bool;
    }
}
