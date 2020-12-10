package com.jbosframework.context;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.context.annotation.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegistry extends BeanRegistry {
    private static final Log log= LogFactory.getLog(AnnotationBeanRegistry.class);

    private AnnotationFilter annotationFilter;
    /**
     * 构造方法
     * @param beanFactory
     */
    public AnnotationBeanRegistry(BeanFactory beanFactory){
        super(beanFactory);
    }

    public void setAnnotationFilter(AnnotationFilter annotationFilter){
        this.annotationFilter=annotationFilter;
    }


    /**
     * 加载Configuration注解
     * @param cls
     */
    private void loadConfigurationAnnotation(Class<?> cls){
        Configuration configuration=cls.getAnnotation(Configuration.class);
        if(configuration!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(configuration.value(),cls);
            annotationBean.setAnnotations(cls.getDeclaredAnnotations());
            this.getBeanFactory().putBeanDefinition(annotationBean);
            //加载配置注解方法Bean
            this.loadConfigurationMethodBean(cls,annotationBean);
        }
    }
    /**
     * 加载配置注解方法Bean
     * @param cls
     * @param parent
     */
    private void loadConfigurationMethodBean(Class<?> cls,AnnotationBean parent){
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        for(int i=0;i<methods.length;i++) {
            Annotation[] annotations = methods[i].getDeclaredAnnotations();
            if (annotations == null||annotations.length<=0) {
                continue;
            }
            for(Annotation annotation:annotations){
                if(annotation instanceof Bean){
                    //加载Bean注解
                    AnnotationBean annotationBean=new AnnotationBean();
                    String id=((Bean)annotation).value();
                    if(StringUtils.isNUll(id)) {
                        id=methods[i].getName();
                    }
                    annotationBean.setId(id);
                    annotationBean.setName(id);
                    annotationBean.setClassName(methods[i].getReturnType().getName());
                    Scope scope=cls.getAnnotation(Scope.class);
                    if(scope!=null){
                        annotationBean.setScope(scope.value());
                    }
                    annotationBean.setAnnotations(annotations);
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setIsMethodBean(true);
                    annotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));
                    if(methods[i].getReturnType().isInterface()){
                        this.getBeanFactory().putBeanNameOfType(methods[i].getReturnType().getName(),annotationBean);
                    }
                    this.getBeanFactory().putBeanDefinition(annotationBean);
                }
            }
        }
    }
    /**
     * 加载Bean注解
     * @param cls
     */
    private void loadBeanAnnotation(Class<?> cls){
        Bean bean=cls.getAnnotation(Bean.class);
        if(bean!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(bean.value(),cls);
            annotationBean.setAnnotations(cls.getDeclaredAnnotations());
            this.getBeanFactory().putBeanDefinition(annotationBean);
            //注册Bean的接口
            this.registryBeanInterfaces(cls,annotationBean);
        }
    }

    /**
     * 加载Service注解
     * @param cls
     */
    private void loadServiceAnnotation(Class<?> cls){
        Service service=cls.getAnnotation(Service.class);
        if(service!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(service.value(),cls);
            annotationBean.setAnnotations(cls.getDeclaredAnnotations());
            this.getBeanFactory().putBeanDefinition(annotationBean);
            //注册Bean的接口
            this.registryBeanInterfaces(cls,annotationBean);
        }
    }
    /**
     * 加载Repository注解
     * @param cls
     */
    private void loadRepositoryAnnotation(Class<?> cls){
        Repository repository=cls.getAnnotation(Repository.class);
        if(repository!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(repository.value(),cls);
            annotationBean.setAnnotations(cls.getDeclaredAnnotations());
            this.getBeanFactory().putBeanDefinition(annotationBean);
            //注册Bean的接口
            this.registryBeanInterfaces(cls,annotationBean);
        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    public void registerBean(Class<?> cls){
        if(cls==null||cls.isAnnotation()){
            return;
        }
        Annotation[] annotations=cls.getDeclaredAnnotations();
        if(annotations==null) {
            return;
        }
        //加载Configuration注解
        this.loadConfigurationAnnotation(cls);
        //加载Bean注解
        this.loadBeanAnnotation(cls);
        //加载Service注解
        this.loadServiceAnnotation(cls);
        //加载Repository注解
        this.loadRepositoryAnnotation(cls);
    }
}
