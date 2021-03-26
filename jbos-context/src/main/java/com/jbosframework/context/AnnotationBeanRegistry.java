package com.jbosframework.context;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.support.BeanRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
/**
 * AnnotationBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegistry extends BeanRegistry {

    private static final Log log= LogFactory.getLog(AnnotationBeanRegistry.class);
    /**
     * 构造方法
     * @param beanFactory
     */
    public AnnotationBeanRegistry(BeanFactory beanFactory){
        super(beanFactory);
    }

    public enum AnnotationType{
        COMPONENT,
        BEAN,
        SERVICE,
        REPOSITORY;

        private AnnotationType(){

        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    private void registerBean(Class<?> cls,AnnotationType annotationType){
        boolean isRegistry=false;
        String beanId="";
        switch(annotationType){
            case COMPONENT:
                Component component=cls.getAnnotation(Component.class);
                if(component!=null){
                    isRegistry=true;
                    beanId=component.value();
                }
            case BEAN:
                Bean bean=cls.getAnnotation(Bean.class);
                if(bean!=null){
                    isRegistry=true;
                    beanId=bean.value();
                }
            case SERVICE:
                Service service=cls.getAnnotation(Service.class);
                if(service!=null){
                    isRegistry=true;
                    beanId=service.value();
                }
            case REPOSITORY:
                Repository repository=cls.getAnnotation(Repository.class);
                if(repository!=null){
                    isRegistry=true;
                    beanId=repository.value();
                }
        }
        if(isRegistry){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(beanId,cls);
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
    public void registry(Class<?> cls){
        if(cls==null||cls.isAnnotation()){
            return;
        }
        Annotation[] annotations=cls.getDeclaredAnnotations();
        if(annotations==null) {
            return;
        }
        //注册Component注解
        this.registerBean(cls,AnnotationType.COMPONENT);
        //注册Bean注解
        this.registerBean(cls,AnnotationType.BEAN);
        //注册Service注解
        this.registerBean(cls,AnnotationType.SERVICE);
        //注册Repository注解
        this.registerBean(cls,AnnotationType.REPOSITORY);
    }
}
