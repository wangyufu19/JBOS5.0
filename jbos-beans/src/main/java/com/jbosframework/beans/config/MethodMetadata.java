package com.jbosframework.beans.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * MethodMetadata
 * @author youfu.wang
 * @version 5.0
 */
public class MethodMetadata {
    private Method method;
    private Annotation[] methodAnnotations;
    private Parameter[] methodParameters;
    private Class<?>[] parameterTypes;

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    public void setMethodParameters(Parameter[] methodParameters){
        this.methodParameters=methodParameters;
    }
    public Parameter[] getMethodParameters(){
        return methodParameters;
    }
    public Annotation[] getMethodAnnotations() {
        return methodAnnotations;
    }

    public void setMethodAnnotations(Annotation[] methodAnnotations) {
        this.methodAnnotations = methodAnnotations;
    }


    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    public static MethodMetadata createMethodMetadata(Method method){
        MethodMetadata methodMetadata=new MethodMetadata();
        methodMetadata.setMethod(method);
        methodMetadata.setMethodAnnotations(method.getDeclaredAnnotations());
        methodMetadata.setMethodParameters(method.getParameters());
        methodMetadata.setParameterTypes(method.getParameterTypes());
        return methodMetadata;
    }
    public boolean isPublic(){
        return (method.getModifiers()& (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC;
    }
    public boolean isAnnotation(Class<?> cls){
        if(methodAnnotations==null){
            return false;
        }
        for(Annotation annotation:methodAnnotations){
            if(annotation.annotationType().getName().equals(cls.getName())){
                return true;
            }
        }
        return false;
    }
    public Annotation findAnnotation(Class<?> cls){
        if(methodAnnotations==null){
            return null;
        }
        for(Annotation annotation:methodAnnotations){
            if(annotation.annotationType().getName().equals(cls.getName())){
                return annotation;
            }
        }
        return null;
    }
}
