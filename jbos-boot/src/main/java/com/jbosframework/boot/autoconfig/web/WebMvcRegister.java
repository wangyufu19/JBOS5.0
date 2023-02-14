package com.jbosframework.boot.autoconfig.web;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.BeanUriUtils;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AnnotationBeanClassParser;
import com.jbosframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.jbosframework.context.annotation.ImportBeanDefinitionSelector;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import com.jbosframework.web.mvc.annotation.RestController;
import com.jbosframework.web.mvc.annotation.WebAnnotationBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class WebMvcRegister implements ImportBeanDefinitionSelector {
    private static final Log logger= LogFactory.getLog(WebMvcRegister.class);
    private ConfigurableApplicationContext applicationContext;

    public WebMvcRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public void processImport(AnnotationBeanClassParser.ConfigurationClass configurationClass) {
        String[] basePackages=new String[1];
        basePackages[0]=applicationContext.getJbosBootClass().getPackage().getName();
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(this.applicationContext.getBeanFactory());
        scanner.addIncludeFilters(Controller.class);
        scanner.addIncludeFilters(RestController.class);
        Set<BeanDefinition> beanDefinitions=scanner.scan(basePackages);
        if(beanDefinitions==null){
            return;
        }
        for(BeanDefinition beanDefinition:beanDefinitions){
            WebAnnotationBean webAnnotationBean=new WebAnnotationBean(beanDefinition.getBeanClass());
            RequestMapping requestMapping=beanDefinition.getBeanClass().getAnnotation(RequestMapping.class);
            if(requestMapping!=null){
                webAnnotationBean.setId(StringUtils.replaceNull(requestMapping.value()));
                webAnnotationBean.setName(StringUtils.replaceNull(requestMapping.value()));
                webAnnotationBean.setRequestMethod(requestMapping.method());
                this.applicationContext.getBeanFactory().putBeanDefinition(webAnnotationBean.getName(),webAnnotationBean);
            }
            this.applicationContext.getBeanFactory().putBeanDefinition(webAnnotationBean.getName(),webAnnotationBean);
            this.loadMethodRequestMapping(webAnnotationBean);
        }
    }
    private void loadMethodRequestMapping(WebAnnotationBean parent){
        Method[] methods=parent.getBeanClass().getDeclaredMethods();
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
                    WebAnnotationBean webAnnotationBean=new WebAnnotationBean(parent.getBeanClass());
                    webAnnotationBean.setParent(parent);
                    webAnnotationBean.setId(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    webAnnotationBean.setName(BeanUriUtils.getBeanUri(parent.getName(), StringUtils.replaceNull(((RequestMapping)annotation).value())));
                    webAnnotationBean.setRequestMethod(((RequestMapping)annotation).method());
                    webAnnotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));
                    this.applicationContext.getBeanFactory().putBeanDefinition(webAnnotationBean.getName(),webAnnotationBean);
                }
            }
        }
    }
}
