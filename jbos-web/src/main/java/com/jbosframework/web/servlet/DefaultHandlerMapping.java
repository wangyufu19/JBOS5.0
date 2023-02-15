package com.jbosframework.web.servlet;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.web.mvc.annotation.RequestMethod;
import com.jbosframework.web.mvc.annotation.ResponseBody;
import com.jbosframework.web.mvc.annotation.WebAnnotationBean;
import com.jbosframework.web.mvc.data.Representation;
import com.jbosframework.web.mvc.dispatcher.Dispatcher;
import com.jbosframework.web.mvc.dispatcher.RequestParameter;
import com.jbosframework.web.utils.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * DefaultHandlerMapping
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultHandlerMapping implements HandlerMapping{
    public static final String DEFAULT_REQUEST_MAPPING="handleRequest";
    private ApplicationContext applicationContext;
    private ServletContext servletContext;

    public DefaultHandlerMapping(ServletContext servletContext){
        this.servletContext = servletContext;
    }
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Dispatcher dispatcher=new Dispatcher(request,response);
        Object controller=null;
        Object ret=null;
        String requestUri = WebUtils.getRequestPath(request);
        controller=this.getRequestController(applicationContext,requestUri);
        if(controller==null){
            dispatcher.doDispatcher404(requestUri);
            return;
        }
        if(controller instanceof GenericBeanDefinition) {
            WebAnnotationBean webAnnotationBean = (WebAnnotationBean) controller;
            if(this.isValidMethod(request,webAnnotationBean.getRequestMethod())){
                ret=this.doHandle(request,response,webAnnotationBean);
                //处理响应
                MethodMetadata methodMetadata=webAnnotationBean.getMethodMetadata();
                if(methodMetadata!=null){
                    Annotation[] methodAnnotations=methodMetadata.getMethodAnnotations();
                    if(methodAnnotations!=null&&methodAnnotations.length>0){
                        ResponseBody responseBody=null;
                        for(Annotation annotation:methodAnnotations){
                            if(annotation instanceof ResponseBody){
                                responseBody=(ResponseBody)annotation;
                                break;
                            }
                        }
                        if(responseBody!=null){
                            dispatcher.doDispatch(requestUri,Dispatcher.APPLICATION_JSON,ret);
                        }else{
                            dispatcher.doDispatch(requestUri,Dispatcher.APPLICATION_TEXT_HTML,ret);
                        }
                    }
                }
            }else{
                dispatcher.doDispatcher404(requestUri);
            }
        }
    }
    private boolean isValidMethod(HttpServletRequest request,RequestMethod[] requestMethods){
        String requestMethod=request.getMethod().toUpperCase();
        for(int i=0;i<requestMethods.length;i++){
            if(requestMethod.equals(requestMethods[i].name())){
                return true;
            }
        }
        return false;
    }
    private Object doHandle(HttpServletRequest request,HttpServletResponse response,WebAnnotationBean webAnnotationBean) throws Exception{
        Object obj=null;
        Object ret=null;
        String methodName=null;
        obj=applicationContext.getBean(webAnnotationBean.getName());
        if(obj==null) return null;
        MethodMetadata methodMetadata=webAnnotationBean.getMethodMetadata();
        if(methodMetadata!=null){
            methodName=methodMetadata.getMethod().getName();
        }
        // 默认调用handleRequest方法
        if ("".equals(methodName)||methodName==null)
            methodName = DEFAULT_REQUEST_MAPPING;
        Representation representation=new Representation(request,response);
        representation.setServletContext(servletContext);
        representation.setApplicationContext(applicationContext);
        RequestParameter requestParameter=new RequestParameter(representation);
        Object[] parameterValues=requestParameter.getParameterValues(webAnnotationBean);
        if(parameterValues!=null&&parameterValues.length>0){
            ret= JBOSClassCaller.call(obj,methodName,parameterValues);
        }else{
            ret=JBOSClassCaller.call(obj,methodName);
        }
        return ret;
    }
    protected Object getRequestController(ApplicationContext applicationContext,String s){
        Object obj=null;
        if(s==null) return null;
        obj=applicationContext.getBeanDefinition(s);
        if(obj==null){
            if(s.indexOf(".form")!=-1){
                if(s.startsWith("/")){
                    s=s.substring(1, s.indexOf(".form"));
                }else{
                    s=s.substring(0, s.indexOf(".form"));
                }
            }
            obj=applicationContext.getBeanDefinition(s);
            obj=obj!=null?obj:applicationContext.getBeanDefinition("/"+s);
        }
        return obj;
    }
}
