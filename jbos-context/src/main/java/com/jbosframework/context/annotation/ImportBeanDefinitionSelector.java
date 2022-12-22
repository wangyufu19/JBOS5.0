package com.jbosframework.context.annotation;

public interface ImportBeanDefinitionSelector{
    void processImport(AnnotationBeanClassParser.ConfigurationClass configurationClass);
}
