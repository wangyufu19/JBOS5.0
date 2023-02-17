package com.jbosframework.web.cors;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.AntPathMatcher;
import com.jbosframework.utils.PathMatcher;
import com.jbosframework.web.servlet.config.CorsRegistration;
import com.jbosframework.web.servlet.config.WebMvcConfigureDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DefaultCorsProcessor implements CorsProcessor {
    private static final Log logger = LogFactory.getLog(DefaultCorsProcessor.class);
    private PathMatcher pathMatcher=new AntPathMatcher();
    private ApplicationContext applicationContext;
    public DefaultCorsProcessor(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }
    public boolean processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!CorsUtils.isCorsRequest(request)) {
            return true;
        }else{
            String requestURI=request.getRequestURI();
            WebMvcConfigureDelegate webMvcConfigureDelegate=this.applicationContext.getBean(WebMvcConfigureDelegate.class);
            List<CorsRegistration> registrations=webMvcConfigureDelegate.getCorsRegistry().getCorsRegistrations();
            if(registrations!=null){
                for(Iterator<CorsRegistration> it = registrations.iterator(); it.hasNext();){
                    CorsRegistration registration=it.next();
                    registration=registration.pathMatcher(pathMatcher);
                    if(registration.isMatch(requestURI)){
                        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                        response.addHeader("Access-Control-Allow-Methods", registration.getAllowedMethods());
                        response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                        response.addHeader("Access-Control-Allow-Credentials", registration.getAllowCredentials());
                        response.addHeader("Access-Control-Max-Age", registration.getMaxAge());
                        return true;
                    }
                }
            }
        }
        return true;
    }


}