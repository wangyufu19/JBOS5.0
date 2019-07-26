package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.core.utils.StringUtils;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import com.jbosframework.web.mvc.annotation.RequestUri;
import java.lang.annotation.Annotation;
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
     * 注册Bean
     * @param cls
     */
    public void registerBean(Class<?> cls){
        if(cls==null||cls.isAnnotation()){
            return;
        }
        Annotation[] annotations=cls.getDeclaredAnnotations();
        if(annotations==null)
            return;
        String id="";
        boolean configurationBean=false;
        for(int i=0;i<annotations.length;i++){
            //判断是否包含的注解
            if(!this.annotationFilter.filter(annotations[i])) {
                continue;
            }
            //判断是否活动配置
            if(!this.isActiveProfile(cls)){
                continue;
            }
            AnnotationBean annotationBean=new AnnotationBean();
            annotationBean.setClassName(cls.getName());
            if(annotations[i] instanceof Bean){
                annotationBean.setId(StringUtils.replaceNull(((Bean)annotations[i]).value()));
                annotationBean.setName(StringUtils.replaceNull(((Bean)annotations[i]).value()));
            }else if(annotations[i] instanceof RequestUri){
                annotationBean.setId(StringUtils.replaceNull(((RequestUri)annotations[i]).value()));
                annotationBean.setName(StringUtils.replaceNull(((RequestUri)annotations[i]).value()));
            }else if(annotations[i] instanceof Controller){
                RequestMapping requestMapping=cls.getAnnotation(RequestMapping.class);
                if(requestMapping!=null){
                    annotationBean.setId(StringUtils.replaceNull(requestMapping.value()));
                    annotationBean.setName(StringUtils.replaceNull(requestMapping.value()));
                    annotationBean.setRequestMethod(StringUtils.replaceNull(requestMapping.method()));
                    annotationBean.setClassMethod(DEFAULT_REQUEST_MAPPING);
                }
            }else if(annotations[i] instanceof Service){
                id=StringUtils.replaceNull(((Service)annotations[i]).value());
                if("".equals(id)) {
                    id=cls.getSimpleName();
                }
                annotationBean.setId(id);
                annotationBean.setName(id);
            }else if(annotations[i] instanceof Repository){
                id=StringUtils.replaceNull(((Repository)annotations[i]).value());
                if("".equals(id)) {
                    id=cls.getSimpleName();
                }
                annotationBean.setId(id);
                annotationBean.setName(id);
            }else{
                continue;
            }
            //设置Scope注解
            this.setBeanScope(cls, annotationBean);
            //注入注解Bean
            this.registryBean(annotationBean);
            //注入类方法注解
            this.registryMethodBean(cls, annotationBean.getName());
        }
    }
}
