package com.jbosframework.boot.context;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceProperties;
import com.jbosframework.boot.autoconfig.jdbc.DataSourcePropertiesBuilder;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ConfigurationPropertiesRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationPropertiesRegistry implements BeanRegistry {
    private static final Log log= LogFactory.getLog(ConfigurationPropertiesRegistry.class);
    private ApplicationContext applicationContext;


    public ConfigurationPropertiesRegistry(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    /**
     * 注册Bean
     * @param cls
     */
    public void registry(Class<?> cls){
        Configuration configuration=cls.getAnnotation(Configuration.class);
        if(configuration==null){
            return;
        }
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        for(int i=0;i<methods.length;i++) {
            Annotation beanAnnotation=methods[i].getDeclaredAnnotation(Bean.class);
            Annotation configurationPropertiesAnnotation=methods[i].getDeclaredAnnotation(ConfigurationProperties.class);
            if(beanAnnotation!=null&&configurationPropertiesAnnotation!=null){
                DataSourceProperties dataSourceProperties=DataSourcePropertiesBuilder.getInstance().create(this.applicationContext,(ConfigurationProperties)configurationPropertiesAnnotation);
                AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(DataSourceProperties.class.getName(),DataSourceProperties.class);
                this.applicationContext.putBeanDefinition(DataSourceProperties.class.getName(),annotationBean);
                this.applicationContext.registerSingletonInstance(DataSourceProperties.class.getName(),dataSourceProperties);
            }
        }
    }
}