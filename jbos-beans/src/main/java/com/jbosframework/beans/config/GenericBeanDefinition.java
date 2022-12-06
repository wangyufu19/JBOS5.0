package com.jbosframework.beans.config;
/**
 * GenericBeanDefinition
 * @author youfu.wang
 * @version 1.0
 */
public class GenericBeanDefinition extends AbstractBeanDefinition{
    private final AnnotationMetadata metadata;
    private MethodMetadata methodMetadata;

    public GenericBeanDefinition(Class<?> beanClass) {
        this.setBeanClass(beanClass);
        this.metadata=new AnnotationMetadata(beanClass);
    }

    public MethodMetadata getMethodMetadata() {
        return methodMetadata;
    }

    public void setMethodMetadata(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }
}
