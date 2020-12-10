package com.jbosframework.web.mvc.annotation;

import com.jbosframework.beans.config.AnnotationBean;

/**
 * WebAnnotationBean
 * @author youfu.wang
 * @version 5.0
 */
public class WebAnnotationBean extends AnnotationBean {

    private RequestMethod[] requestMethod;

    public RequestMethod[] getRequestMethod() {
        return requestMethod;
    }
    public void setRequestMethod(RequestMethod[] requestMethod) {
        this.requestMethod = requestMethod;
    }

}
