package com.jbosframework.web.servlet;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.AntPathMatcher;
import com.jbosframework.utils.PathMatcher;
import com.jbosframework.web.servlet.config.InterceptorRegistration;
import com.jbosframework.web.servlet.config.WebMvcConfigureDelegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * DefaultHandlerMapping
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultHandlerMapping implements HandlerMapping{
    private ApplicationContext applicationContext;
    private PathMatcher pathMatcher=new AntPathMatcher();

    public DefaultHandlerMapping(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public HandlerExecutionChain getHandlerExecutionChain(HttpServletRequest request, HttpServletResponse response) {
        String requestURI=request.getRequestURI();
        HandlerExecutionChain handlerExecutionChain=new HandlerExecutionChain(request,response);
        WebMvcConfigureDelegate webMvcConfigureDelegate=this.applicationContext.getBean(WebMvcConfigureDelegate.class);
        List<InterceptorRegistration> registrations=webMvcConfigureDelegate.getInterceptorRegistry().getInterceptors();
        if(registrations!=null){
            for(Iterator<InterceptorRegistration> it = registrations.iterator();it.hasNext();){
                InterceptorRegistration registration=it.next();
                registration=registration.pathMatcher(pathMatcher);
                if(registration.isMatch(requestURI)){
                    handlerExecutionChain.addInterceptor(registration.getInterceptor());
                }
            }
        }
        return handlerExecutionChain;
    }
}
