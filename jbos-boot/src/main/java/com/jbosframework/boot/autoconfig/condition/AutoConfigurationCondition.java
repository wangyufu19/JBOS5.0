package com.jbosframework.boot.autoconfig.condition;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.Condition;
import com.jbosframework.context.annotation.Conditional;
import com.jbosframework.utils.JBOSClassloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AutoConfigurationCondition
 * @author youfu.wang
 */

public abstract class AutoConfigurationCondition {
    private static final Log log= LogFactory.getLog(AutoConfigurationCondition.class);
    private ConfigurableApplicationContext ctx;


    public void setApplicationContext(ConfigurableApplicationContext ctx){
        this.ctx=ctx;
    }
    public ApplicationContext getApplicationContext(){
        return this.ctx;
    }

    public boolean conditionalOnClass(ConditionalOnClass conditionOnClass){
        if (conditionOnClass!=null){
            Class<?>[] classes=conditionOnClass.value();
            Conditional conditionalAnnotation=ConditionalOnClass.class.getDeclaredAnnotation(Conditional.class);
            if(conditionalAnnotation!=null){
                Condition condition=(Condition)JBOSClassloader.newInstance(conditionalAnnotation.value());
                return condition.matches();
            }
        }
        return false;
    }

    public boolean conditionalOnBean(ConditionalOnBean conditionalOnBean){
        if (conditionalOnBean!=null){
            Class<?>[] classes=conditionalOnBean.value();
            Conditional conditionalAnnotation=ConditionalOnBean.class.getDeclaredAnnotation(Conditional.class);
            if(conditionalAnnotation!=null){
                Condition condition=(Condition)JBOSClassloader.newInstance(conditionalAnnotation.value());
                return condition.matches();
            }
        }
        return false;
    }

    public boolean conditionalOnProperty(ConditionalOnProperty conditionalOnProperty){
        if(conditionalOnProperty!=null){
            Conditional conditionalAnnotation=ConditionalOnProperty.class.getDeclaredAnnotation(Conditional.class);
            if(conditionalAnnotation!=null){
                Condition condition=(Condition)JBOSClassloader.newInstance(conditionalAnnotation.value());
                return condition.matches();
            }
        }
        return false;
    }

}
