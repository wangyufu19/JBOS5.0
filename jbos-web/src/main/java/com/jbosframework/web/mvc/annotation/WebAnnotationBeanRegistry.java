package com.jbosframework.web.mvc.annotation;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * WebAnnotationBeanRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class WebAnnotationBeanRegistry extends BeanRegistry {
    public static final String DEFAULT_REQUEST_MAPPING="handleRequest";

    /**
     * 构造方法
     * @param beanFactory
     */
    public WebAnnotationBeanRegistry(BeanFactory beanFactory){
        super(beanFactory);
    }
    /**
     * 加载Controller注解
     * @param cls
     */
    private void loadControllerAnnotation(Class<?> cls){
        Controller controller=cls.getAnnotation(Controller.class);
        if(controller==null){
            return;
        }
        AnnotationBean annotationBean=new AnnotationBean();
        annotationBean.setClassName(cls.getName());
        RequestMapping requestMapping=cls.getAnnotation(RequestMapping.class);
        if(requestMapping!=null){
            annotationBean.setId(StringUtils.replaceNull(requestMapping.value()));
            annotationBean.setName(StringUtils.replaceNull(requestMapping.value()));
            annotationBean.setRequestMethod(StringUtils.replaceNull(requestMapping.method()));
            annotationBean.setClassMethod(DEFAULT_REQUEST_MAPPING);
            this.getBeanFactory().putBeanDefinition(annotationBean);
        }
        //注入类方法注解
        this.loadClassMethodAnnotation(cls,annotationBean);
    }
    /**
     * 加载类方法注解
     * @param cls
     * @param parent
     */
    private void loadClassMethodAnnotation(Class<?> cls,AnnotationBean parent){
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
                if(annotation instanceof RequestMapping){
                    //加载RequestMapping注解
                    AnnotationBean annotationBean=new AnnotationBean();
                    annotationBean.setAnnotations(annotations);
                    annotationBean.setClassName(cls.getName());
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setScope(Scope.SCOPE_PROTOTYPE);
                    annotationBean.setId(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setName(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setRequestMethod(StringUtils.replaceNull(((RequestMapping)annotation).method()));
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                    annotationBean.setParentName(parent.getName());
                    this.getBeanFactory().putBeanDefinition(annotationBean);
                }
            }
        }
    }
    public void registerBean(Class<?> cls) {
        //加载Controller注解
        this.loadControllerAnnotation(cls);
    }
}
