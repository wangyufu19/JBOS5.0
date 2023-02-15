package com.jbosframework.web.servlet;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.servlet.config.InterceptorRegistration;
import com.jbosframework.web.servlet.config.InterceptorRegistry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

public class DefaultHandlerMapping implements HandlerMapping{
    private ApplicationContext applicationContext;

    public DefaultHandlerMapping(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public HandlerExecutionChain getHandlerExecutionChain(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecutionChain handlerExecutionChain=new HandlerExecutionChain(request,response);
        InterceptorRegistry interceptorRegistry=this.applicationContext.getBean(InterceptorRegistry.class);
        List<InterceptorRegistration> registrations=interceptorRegistry.getInterceptors();
        if(null!=registrations){
            for(Iterator<InterceptorRegistration> it = registrations.iterator();it.hasNext();){
                handlerExecutionChain.addInterceptor(it.next().getInterceptor());
            }
        }
        return handlerExecutionChain;
    }
}
