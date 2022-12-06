package com.jbosframework.web.mvc.annotation;

import com.jbosframework.beans.config.GenericBeanDefinition;

/**
 * WebAnnotationBean
 * @author youfu.wang
 * @version 5.0
 */
public class WebAnnotationBean extends GenericBeanDefinition {

    private RequestMethod[] requestMethod;

    public WebAnnotationBean(Class<?> beanClass) {
        super(beanClass);
    }

    public RequestMethod[] getRequestMethod() {
        return requestMethod;
    }
    public void setRequestMethod(RequestMethod[] requestMethod) {
        this.requestMethod = requestMethod;
    }

}
