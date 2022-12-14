package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;
import com.jbosframework.utils.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;

/**
 * OnClassCondition
 * @author youfu.wang
 * @version 1.0
 */
public class OnClassCondition extends AbstractCondition {
    private static final Log logger= LogFactory.getLog(OnClassCondition.class);
    private Annotation annotation;

    public OnClassCondition(ConfigurableApplicationContext applicationContext, Annotation annotation){
        super(applicationContext);
        this.annotation=annotation;
    }
    public boolean matches() {
        boolean bool=false;
        if(this.annotation==null){
            return false;
        }
        Class<?>[] classes=((ConditionalOnClass)this.annotation).value();
        if (classes!=null){
            for(Class<?> cls:classes){
                bool=JBOSClassloader.isPresent(cls.getName());
            }
        }
        return bool;
    }
}
