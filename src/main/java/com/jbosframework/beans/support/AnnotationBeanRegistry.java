package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.common.utils.StringUtils;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegistry extends BeanRegistry{
    public static final String DEFAULT_REQUEST_MAPPING="handleRequest";
    private AnnotationFilter annotationFilter;

    /**
     * 构造方法
     * @param ctx
     */
    public AnnotationBeanRegistry(ApplicationContext ctx){
        super(ctx);
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
        if(configuration==null){
            return;
        }
        AnnotationBean annotationBean=new AnnotationBean();
        String id=configuration.value();
        if(StringUtils.isNUll(id)) {
            id=cls.getSimpleName();
        }
        annotationBean.setId(id);
        annotationBean.setName(id);
        annotationBean.setClassName(cls.getName());
        //设置Scope注解
        this.setBeanScope(cls, annotationBean);
        //注入注解Bean
        this.registryBean(annotationBean);
        //注入类方法注解
        this.loadClassMethodAnnotation(cls,annotationBean);
    }
    /**
     * 加载Bean注解
     * @param cls
     */
    private void loadBeanAnnotation(Class<?> cls){
        Bean bean=cls.getAnnotation(Bean.class);
        if(bean==null){
            return;
        }
        AnnotationBean annotationBean=new AnnotationBean();
        annotationBean.setId(StringUtils.replaceNull(bean.value()));
        annotationBean.setName(StringUtils.replaceNull(bean.value()));
        annotationBean.setClassName(cls.getName());
        //设置Scope注解
        this.setBeanScope(cls, annotationBean);
        //注入注解Bean
        this.registryBean(annotationBean);
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
            //设置Scope注解
            this.setBeanScope(cls, annotationBean);
            //注入注解Bean
            this.registryBean(annotationBean);
        }
        //注入类方法注解
        this.loadClassMethodAnnotation(cls,annotationBean);
    }
    /**
     * 加载Service注解
     * @param cls
     */
    private void loadServiceAnnotation(Class<?> cls){
        Service service=cls.getAnnotation(Service.class);
        if(service==null){
            return;
        }
        AnnotationBean annotationBean=new AnnotationBean();
        String id=service.value();
        if(StringUtils.isNUll(id)) {
            id=cls.getSimpleName();
        }
        annotationBean.setId(id);
        annotationBean.setName(id);
        annotationBean.setClassName(cls.getName());
        //设置Scope注解
        this.setBeanScope(cls, annotationBean);
        //注入注解Bean
        this.registryBean(annotationBean);
    }
    /**
     * 加载Repository注解
     * @param cls
     */
    private void loadRepositoryAnnotation(Class<?> cls){
        Repository repository=cls.getAnnotation(Repository.class);
        if(repository==null){
            return;
        }
        AnnotationBean annotationBean=new AnnotationBean();
        String id=repository.value();
        if(StringUtils.isNUll(id)) {
            id=cls.getSimpleName();
        }
        annotationBean.setId(id);
        annotationBean.setName(id);
        annotationBean.setClassName(cls.getName());
        //设置Scope注解
        this.setBeanScope(cls, annotationBean);
        //注入注解Bean
        this.registryBean(annotationBean);
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
            Annotation[] annotations = methods[i].getAnnotations();
            if (annotations == null||annotations.length<=0) {
                continue;
            }
            AnnotationBean annotationBean=new AnnotationBean();
            annotationBean.setClassName(cls.getName());
            annotationBean.setParentName(parent.getName());
            //加载Bean注解
            Bean bean=methods[i].getAnnotation(Bean.class);
            if(bean!=null){
                String id=bean.value();
                if(StringUtils.isNUll(id)) {
                    id=methods[i].getReturnType().getSimpleName();
                }
                annotationBean.setId(id);
                annotationBean.setName(id);
                annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                annotationBean.setIsMethodBean(true);
                //注入注解Bean
                this.registryBean(annotationBean);
            }
            //加载RequestMapping注解
            RequestMapping requestMapping=methods[i].getAnnotation(RequestMapping.class);
            if(requestMapping!=null){
                annotationBean.setName(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(requestMapping.value())));
                annotationBean.setRequestMethod(StringUtils.replaceNull(requestMapping.method()));
                annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                //注入注解Bean
                this.registryBean(annotationBean);
            }
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
        //加载Controller注解
        this.loadControllerAnnotation(cls);
        //加载Service注解
        this.loadServiceAnnotation(cls);
        //加载Repository注解
        this.loadRepositoryAnnotation(cls);
    }
}
