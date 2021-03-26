package com.jbosframework.web.mvc.annotation;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.MethodMetadata;
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
        WebAnnotationBean webAnnotationBean=new WebAnnotationBean();
        webAnnotationBean.setClassName(cls.getName());
        RequestMapping requestMapping=cls.getAnnotation(RequestMapping.class);
        if(requestMapping!=null){
            webAnnotationBean.setId(StringUtils.replaceNull(requestMapping.value()));
            webAnnotationBean.setName(StringUtils.replaceNull(requestMapping.value()));
            webAnnotationBean.setRequestMethod(requestMapping.method());
            this.getBeanFactory().putBeanDefinition(webAnnotationBean);
        }
        //注入类方法注解
        this.loadClassMethodAnnotation(cls,webAnnotationBean);
    }
    /**
     * 加载类方法注解
     * @param cls
     * @param parent
     */
    private void loadClassMethodAnnotation(Class<?> cls,WebAnnotationBean parent){
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
                    WebAnnotationBean annotationBean=new WebAnnotationBean();
                    annotationBean.setAnnotations(annotations);
                    annotationBean.setClassName(cls.getName());
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setScope(Scope.SCOPE_PROTOTYPE);
                    annotationBean.setId(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setName(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setRequestMethod(((RequestMapping)annotation).method());
                    annotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));
                    annotationBean.setParentName(parent.getName());
                    this.getBeanFactory().putBeanDefinition(annotationBean);
                }
            }
        }
    }
    public void registry(Class<?> cls) {
        //加载Controller注解
        Controller controller=cls.getAnnotation(Controller.class);
        if(controller!=null){
            this.loadControllerAnnotation(cls);
        }
        //加载RestController注解
        RestController restController=cls.getAnnotation(RestController.class);
        if(restController!=null){
            this.loadControllerAnnotation(cls);
        }
    }
}
