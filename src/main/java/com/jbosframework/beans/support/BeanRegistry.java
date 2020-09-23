package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.Profile;
import com.jbosframework.context.configuration.Environment;
import com.jbosframework.common.utils.StringUtils;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import com.jbosframework.web.mvc.annotation.RequestUri;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class BeanRegistry {
    private ApplicationContext ctx;

    /**
     * 构造方法
     * @param ctx
     */
    public BeanRegistry(ApplicationContext ctx){
        this.ctx=ctx;
    }
    /**
     * 是否活动配置
     * @param cls
     * @return
     */
    public boolean isActiveProfile(Class<?> cls){
        boolean bool=true;
        Profile profile=cls.getAnnotation(Profile.class);
        //读取指定配置文件的Bean对象
        if(profile!=null) {
            Environment environment=this.ctx.getContextConfiguration().getEnvironment();
            if(!environment.getActiveProfiles().equals(profile.value())) {
                bool=false;
            }
        }
        return bool;
    }
    /**
     * 设置Scope注解(singleton,prototype)
     * @param annClass
     * @param annotationBean
     */
    public void setBeanScope(Class<?> annClass,AnnotationBean annotationBean){
        Scope scope=annClass.getAnnotation(Scope.class);
        if(scope!=null){
            annotationBean.setScope(scope.value());
        }
    }
    /**
     * 注册Bean
     * @param annotationBean
     */
    public void registryBean(AnnotationBean annotationBean){
        if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
            this.ctx.putBeanDefinition(annotationBean);
        }
    }
    /**
     * 注入类方法注解
     * @param cls
     * @param parentName
     */
    public void registryMethodBean(Class<?> cls,String parentName){
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
            annotationBean.setParentName(parentName);
            annotationBean.setClassName(cls.getName());
            for(int j=0;j<annotations.length;j++){
                if(annotations[j] instanceof Bean){
                    Bean bean=(Bean)annotations[j];
                    id= StringUtils.replaceNull(bean.value());
                    if("".equals(id)) {
                        id=StringUtils.replaceNull(methods[i].getName());
                    }
                    annotationBean.setId(id);
                    annotationBean.setName(id);
                    annotationBean.setIsMethodBean(true);
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                    Profile profile=methods[i].getAnnotation(Profile.class);
                    if(profile!=null) {
                        Environment environment=this.ctx.getContextConfiguration().getEnvironment();
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
                if(annotationBean.isMethodBean()){
                    annotationBean.setScope(Scope.SCOPE_PROTOTYPE);
                }else{
                    //设置Scope注解
                    this.setBeanScope(cls, annotationBean);
                }
                //注入注解Bean
                this.registryBean(annotationBean);
            }
        }
    }
}
