package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.AnnotationScanFactory;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.context.configuration.Environment;
import com.jbosframework.context.annotation.Profile;
import com.jbosframework.core.utils.StringUtils;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import com.jbosframework.web.mvc.annotation.RequestUri;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationBeanRegister
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegister {
    public static final String DEFAULT_REQUEST_MAPPING="handleRequest";
    private AnnotationScanFactory annotationScanFactory;

    /**
     * 构造方法
     * @param annotationScanFactory
     */
    public AnnotationBeanRegister(AnnotationScanFactory annotationScanFactory){
        this.annotationScanFactory=annotationScanFactory;
    }
    /**
     * 是否活动配置
     * @param cls
     * @return
     */
    private boolean isActiveProfile(Class<?> cls){
        boolean bool=true;
        Profile profile=cls.getAnnotation(Profile.class);
        //读取指定配置文件的Bean对象
        if(profile!=null) {
            Environment environment=this.annotationScanFactory.getContextConfiguration().getEnvironment();
            if(!environment.getActiveProfiles().equals(profile.value())) {
                bool=false;
            }
        }
        return bool;
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
        String parentName="";
        for(int i=0;i<annotations.length;i++){
            //判断是否包含的注解
            if(!this.annotationScanFactory.getAnnotationFilter().filter(annotations[i])) {
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
            setScopeAnnotation(cls, annotationBean);
            //注入注解Bean
            if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
                this.annotationScanFactory.putBean(annotationBean);
            }
            //注入类方法注解
            this.loadMethodAnnotationBean(cls, parentName);
        }
    }
    /**
     * 注入类方法注解
     * @param cls
     * @param parentName
     */
    private void loadMethodAnnotationBean(Class<?> cls,String parentName) {
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        String id="";
        for(int i=0;i<methods.length;i++){
            Annotation[] annotations=methods[i].getAnnotations();
            if(annotations==null) {
                continue;
            }
            AnnotationBean annotationBean=new AnnotationBean();
            annotationBean.setClassName(cls.getName());
            for(int j=0;j<annotations.length;j++){
                if(annotations[j] instanceof Bean){
                    Bean bean=(Bean)annotations[j];
                    id=StringUtils.replaceNull(bean.value());
                    if("".equals(id)) {
                        id=StringUtils.replaceNull(methods[i].getName());
                    }
                    annotationBean.setId(id);
                    annotationBean.setName(id);
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                    Profile profile=methods[i].getAnnotation(Profile.class);
                    if(profile!=null) {
                        Environment environment=this.annotationScanFactory.getContextConfiguration().getEnvironment();
                        if(!environment.getActiveProfiles().equals(profile.value())) {
                            continue;
                        }
                    }
                }else if(annotations[j] instanceof RequestUri){
                    RequestUri requestUri=(RequestUri)annotations[j];
                    annotationBean.setName(BeanUriUtils.getBeanUri(parentName, StringUtils.replaceNull(requestUri.value())));
                    annotationBean.setRequestMethod(StringUtils.replaceNull(requestUri.method()));
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                }else if(annotations[j] instanceof RequestMapping){
                    RequestMapping requestMapping=(RequestMapping)annotations[j];
                    annotationBean.setName(BeanUriUtils.getBeanUri(parentName, StringUtils.replaceNull(requestMapping.value())));
                    annotationBean.setRequestMethod(StringUtils.replaceNull(requestMapping.method()));
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                }else{
                    continue;
                }
                //设置Scope注解
                setScopeAnnotation(cls, annotationBean);
                //注入注解Bean
                if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
                    this.annotationScanFactory.putBean(annotationBean);
                }
            }
        }
    }
    /**
     * 设置Scope注解(singleton,prototype)
     * @param annClass
     * @param annotationBean
     */
    private void setScopeAnnotation(Class<?> annClass,AnnotationBean annotationBean){
        Scope scope=annClass.getAnnotation(Scope.class);
        if(scope!=null){
            annotationBean.setScope(scope.value());
        }
    }
}
