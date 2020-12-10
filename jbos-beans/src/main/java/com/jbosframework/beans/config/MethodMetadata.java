package com.jbosframework.beans.config;

import com.jbosframework.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * MethodMetadata
 * @author youfu.wang
 * @version 5.0
 */
public class MethodMetadata {
    private String methodName;
    private Parameter[] methodParameters;
    private Annotation[] methodAnnotations;
    private Class<?>[] ParameterTypes;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
        return ParameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        ParameterTypes = parameterTypes;
    }
    public static MethodMetadata createMethodMetadata(Method method){
        MethodMetadata methodMetadata=new MethodMetadata();
        methodMetadata.setMethodName(StringUtils.replaceNull(method.getName()));
        methodMetadata.setMethodAnnotations(method.getDeclaredAnnotations());
        methodMetadata.setMethodParameters(method.getParameters());
        methodMetadata.setParameterTypes(method.getParameterTypes());
        return methodMetadata;
    }
}
