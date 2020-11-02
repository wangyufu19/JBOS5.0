package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * AnnotationBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegistry extends BeanRegistry{
    private static final Log log= LogFactory.getLog(AnnotationBeanRegistry.class);
    public static final String DEFAULT_REQUEST_MAPPING="handleRequest";

    private AnnotationFilter annotationFilter;

    /**
     * 构造方法
     */
    public AnnotationBeanRegistry(){

    }
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
        if(configuration!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(configuration.value(),cls);
            //注入注解Bean
            this.registryBean(cls,annotationBean);
            //注入类方法注解
            this.loadClassMethodAnnotation(cls,annotationBean);
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
            //注入注解Bean
            this.registryBean(cls,annotationBean);
            //注册Bean的接口
            this.registryBeanInterfaces(cls,annotationBean);
        }
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
            //注入注解Bean
            this.registryBean(cls,annotationBean);
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
        if(service!=null){
            AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(service.value(),cls);
            //注入注解Bean
            this.registryBean(cls,annotationBean);
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
            //注入注解Bean
            this.registryBean(cls,annotationBean);
            //注册Bean的接口
            this.registryBeanInterfaces(cls,annotationBean);
        }
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
                if(annotation instanceof Bean){
                    //加载Bean注解
                    AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(((Bean)annotation).value(),methods[i].getReturnType());
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                    annotationBean.setIsMethodBean(true);
                    annotationBean.setScope(Scope.SCOPE_PROTOTYPE);
                    if(methods[i].getReturnType().isInterface()){
                        this.registryBeanInterfaces(methods[i].getReturnType().getName(),annotationBean);
                    }
                    //注入注解Bean
                    this.registryBean(methods[i].getClass(),annotationBean);
                }else if(annotation instanceof RequestMapping){
                    AnnotationBean annotationBean=new AnnotationBean();
                    annotationBean.setClassName(cls.getName());
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setId(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setName(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    annotationBean.setRequestMethod(StringUtils.replaceNull(((RequestMapping)annotation).method()));
                    annotationBean.setClassMethod(StringUtils.replaceNull(methods[i].getName()));
                    annotationBean.setParentName(parent.getName());
                    //注入注解Bean
                    this.registryBean(methods[i].getClass(),annotationBean);
                }
            }
        }
    }
    /**
     * 设置Scope注解(singleton,prototype)
     * @param annClass
     * @param annotationBean
     */
    private void setBeanScope(Class<?> annClass,AnnotationBean annotationBean){
        Scope scope=annClass.getAnnotation(Scope.class);
        if(scope!=null){
            annotationBean.setScope(scope.value());
        }
    }
    /**
     * 注册Bean
     * @param annotationBean
     */
    private void registryBean(Class<?> annClass,AnnotationBean annotationBean){
        if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
            this.setBeanScope(annClass,annotationBean);
            this.getApplicationContext().putBeanDefinition(annotationBean);
        }
    }
    //注册Bean的接口
    private void registryBeanInterfaces(Class<?> cls,AnnotationBean annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.getApplicationContext().putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    //注册Bean的接口
    private void registryBeanInterfaces(String interfaceName,AnnotationBean annotationBean){
        this.getApplicationContext().putBeanNameOfType(interfaceName,annotationBean);
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
