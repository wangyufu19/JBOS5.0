package com.jbosframework.web.mvc.dispatcher;

import com.jbosframework.beans.access.BeanFactory;
import com.jbosframework.beans.access.Setter;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.JsonUtils;
import com.jbosframework.web.mvc.annotation.RequestBody;
import com.jbosframework.web.mvc.annotation.WebAnnotationBean;
import com.jbosframework.web.mvc.data.Representation;
import com.jbosframework.web.utils.TypeConverter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * RequestParameter
 * @author youfu.wang
 * @version 5.0
 */
public class RequestParameter {
    private Representation representation;

    public RequestParameter(Representation representation){
        this.representation=representation;
    }
    public Object[] getParameterValues(WebAnnotationBean webAnnotationBean){
        Object[] parameterValues=null;
        Class parameterClass=null;
        Object parameterObject=null;
        Field[] parameterClassFields=null;
        Parameter[] parameters=webAnnotationBean.getMethodMetadata().getMethodParameters();
        if(parameters!=null&&parameters.length>0){
            parameterValues=new Object[parameters.length];
            for(int i=0;i<parameters.length;i++){
                Parameter parameter=parameters[i];
                Class<?> type=parameter.getType();
                if(type.getName().equals(Representation.class.getName())){
                    parameterValues[i]=representation;
                }else if(type.getName().equals(Map.class.getName())){
                    RequestBody requestBody=parameter.getAnnotation(RequestBody.class);
                    if(requestBody!=null){
                        parameterValues[i]=JsonUtils.fromJson(representation.getRequestBody(representation.getRequest().getCharacterEncoding()),Map.class);
                    }
                }else if(TypeConverter.isPrimitiveType(type.getName())){
                    parameterValues[i]=representation.getParameter(parameter.getName(),true);
                }else{
                    //设置POJO对象字段值
                    try {
                        parameterClass= JBOSClassloader.loadClass(type.getName());
                        if(parameterClass!=null){
                            parameterObject=parameterClass.newInstance();
                            parameterClassFields=parameterClass.getDeclaredFields();
                            for(Field field:parameterClassFields){
                                if(field.getType().isInterface()){
                                    continue;
                                }
                                Setter setter= BeanFactory.getSetter(type, field.getName());
                                if(TypeConverter.isPrimitiveType(field.getType())){
                                    setter.set(parameterObject,representation.getParameter(field.getName(), false));
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return parameterValues;
    }

}
