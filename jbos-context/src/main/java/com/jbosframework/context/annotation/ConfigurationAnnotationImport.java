package com.jbosframework.context.annotation;

import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.schedule.annotation.AsyncConfiguration;
import com.jbosframework.schedule.annotation.EnableAsync;
import com.jbosframework.schedule.annotation.EnableScheduling;
import com.jbosframework.schedule.annotation.ScheduleConfiguration;
import com.jbosframework.utils.JBOSClassloader;

/**
 * ConfigurationAnnotationImport
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationAnnotationImport {
    private BeanFactory beanFactory;

    public ConfigurationAnnotationImport(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public enum AnnotationType{
        EnableAsync,
        EnableScheduling;

        private AnnotationType(){

        }
    }

    public void doImport(Class<?> configurationCls,AnnotationType annotationType){
        Class<?>[] importClasses=null;
        Import importAnnotation=null;
        if(annotationType==AnnotationType.EnableAsync){
            EnableAsync enableAsync=configurationCls.getAnnotation(EnableAsync.class);
            importAnnotation=enableAsync.getClass().getAnnotation(Import.class);
        }else if(annotationType==AnnotationType.EnableScheduling){
            EnableScheduling enableScheduling=configurationCls.getAnnotation(EnableScheduling.class);
            importAnnotation=enableScheduling.getClass().getAnnotation(Import.class);
        }
        if(importAnnotation!=null){
            importClasses=importAnnotation.value();
        }
        if(importClasses!=null){
            for(Class cls:importClasses){
                Object obj= JBOSClassloader.newInstance(cls);
                if(obj instanceof AsyncConfiguration){
                    AsyncConfiguration asyncConfiguration=(AsyncConfiguration)obj;
                    asyncConfiguration.setBeanFactory(this.beanFactory);
                    asyncConfiguration.registryBeanProcessor();
                }else if(obj instanceof ScheduleConfiguration){
                    ScheduleConfiguration scheduleConfiguration=(ScheduleConfiguration)obj;
                    scheduleConfiguration.setBeanFactory(this.beanFactory);
                    scheduleConfiguration.registryBeanProcessor();
                }
            }
        }
    }
}
