package com.jbosframework.boot.autoconfig.condition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OnBeanCondition
 * @author youfu.wang
 */
public class OnBeanCondition extends AbstractCondition {
    private static final Log logger= LogFactory.getLog(OnBeanCondition.class);
    private Class<?>[] classes;

    public OnBeanCondition(ConfigurableApplicationContext ctx, Class<?>[] classes){
        super(ctx);
        this.classes=classes;
    }
    public boolean matches() {
        boolean bool=false;
        if(classes!=null){
            for(Class<?> cls:classes){
                bool= this.getApplicationContext().containsBean(cls.getName());
            }
        }
        return bool;
    }
}
