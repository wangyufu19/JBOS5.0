package com.jbosframework.beans.config;

import java.lang.annotation.Annotation;

public class AnnotationMetadata {

    private Annotation[] annotations;

    public AnnotationMetadata(){

    }
    public AnnotationMetadata(Class<?> beanClass){
        this.annotations=beanClass.getAnnotations();
    }
    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
    public boolean isAnnotation(Class<?> cls){
        if(annotations==null){
            return false;
        }
        for(Annotation annotation:annotations){
            if(annotation.annotationType().getName().equals(cls.getName())){
                return true;
            }
        }
        return false;
    }
    public Annotation findAnnotation(Class<?> cls){
        if(annotations==null){
            return null;
        }
        for(Annotation annotation:annotations){
            if(annotation.annotationType().getName().equals(cls.getName())){
                return annotation;
            }
        }
        return null;
    }
}
