package com.jbosframework.beans.config;

import com.jbosframework.core.annotaion.AnnotationUtils;

import java.lang.annotation.Annotation;

public class AnnotationMetadata {

    private Annotation[] annotations;

    public AnnotationMetadata(Class<?> beanClass){
        this.annotations=beanClass.getAnnotations();
    }
    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
    public Annotation getAnnotation(Class<?> cls){
        Annotation result=null;
        if(annotations!=null){
            for(Annotation annotation:annotations){
                if(annotation.annotationType().getName().equals(cls.getName())){
                    result=annotation;
                    break;
                }
            }
        }
        return result;
    }
}
