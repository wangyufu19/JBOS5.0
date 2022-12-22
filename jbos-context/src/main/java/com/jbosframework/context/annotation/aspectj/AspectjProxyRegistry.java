package com.jbosframework.context.annotation.aspectj;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.aspectj.*;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AnnotationBeanClassParser;
import com.jbosframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.jbosframework.context.annotation.ImportBeanDefinitionSelector;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class AspectjProxyRegistry implements ImportBeanDefinitionSelector {
    private static final Log logger= LogFactory.getLog(AspectjProxyRegistry.class);
    private ConfigurableApplicationContext applicationContext;

    public AspectjProxyRegistry(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void processImport(AnnotationBeanClassParser.ConfigurationClass configurationClass){
        String[] basePackages=new String[1];
        basePackages[0]=configurationClass.getSource().getPackage().getName();
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(this.applicationContext.getBeanFactory());
        scanner.addIncludeFilters(Aspect.class);
        Set<BeanDefinition> beanDefinitions=scanner.scan(basePackages);
        if(beanDefinitions!=null){
            for(BeanDefinition beanDefinition:beanDefinitions){
                this.registryAspectjMetadata(beanDefinition);
            }
        }
    }
    private void registryAspectjMetadata(BeanDefinition beanDefinition){
        this.applicationContext.getBeanFactory().putBeanDefinition(beanDefinition.getClassName(),beanDefinition);
        Class cls=beanDefinition.getBeanClass();
        if(cls==null){
            return;
        }
        Method[] methods=cls.getDeclaredMethods();
        if(methods==null){
            return;
        }
        AspectMetadata metadata=new AspectMetadata();
        AdviceConfig adviceConfig=new AdviceConfig();
        AspectjMethodAdvice aspectjMethodAdvice=new AspectjMethodAdvice();

        for(int i=0;i<methods.length;i++) {
            Annotation[] annotations = methods[i].getAnnotations();
            if (annotations == null) {
                continue;
            }
            for(int j=0;j<annotations.length;j++) {
                if (annotations[j] instanceof Pointcut) {
                    Pointcut pointcut=(Pointcut)annotations[j];
                    metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                }
                if(annotations[j] instanceof Before){
                    Before pointcut=(Before)annotations[j];
                    if(StringUtils.isNotNUll(pointcut.value())){
                        metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                    }
                    AspectJMethodInvocation aspectJPointcut=new AspectJMethodInvocation(cls,methods[i],null);
                    AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice=new AspectjMethodBeforeAdvice(aspectJPointcut);
                    aspectjMethodAdvice.setAspectjMethodBeforeAdvice(aspectjMethodBeforeAdvice);
                }else if(annotations[j] instanceof After){
                    After pointcut=(After)annotations[j];
                    if(StringUtils.isNotNUll(pointcut.value())){
                        metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                    }
                    AspectJMethodInvocation aspectJPointcut=new AspectJMethodInvocation(cls,methods[i],null);
                    AspectjMethodAfterAdvice aspectjMethodAfterAdvice=new AspectjMethodAfterAdvice(aspectJPointcut);
                    aspectjMethodAdvice.setAspectjMethodAfterAdvice(aspectjMethodAfterAdvice);
                }
            }
        }
        adviceConfig.setMethodAdvisor(aspectjMethodAdvice);
        metadata.setAdviceConfig(adviceConfig);
        if(StringUtils.isNotNUll(metadata.getPointcut())){
            AspectjApplicationContext aspectjApplicationContext=new AspectjApplicationContext(this.applicationContext);
            aspectjApplicationContext.putAspectMetadata(metadata);
            AspectjBeanPostProcessor aspectjBeanPostProcessor=new AspectjBeanPostProcessor(aspectjApplicationContext);
            this.applicationContext.getBeanFactory().registerBeanPostProcessor(aspectjBeanPostProcessor);
        }
    }
}
