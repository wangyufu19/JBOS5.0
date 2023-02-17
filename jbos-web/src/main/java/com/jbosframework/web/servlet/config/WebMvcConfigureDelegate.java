package com.jbosframework.web.servlet.config;
import java.util.Iterator;
import java.util.List;


public class WebMvcConfigureDelegate {
    private InterceptorRegistry interceptorRegistry=new InterceptorRegistry();
    private CorsRegistry corsRegistry=new CorsRegistry();

    public void addRegistry(List<WebMvcConfigurer> configurers) {
        for(Iterator<WebMvcConfigurer> it=configurers.iterator();it.hasNext();){
            WebMvcConfigurer webMvcConfigurer=it.next();
            webMvcConfigurer.addCorsMappings(corsRegistry);
            webMvcConfigurer.addInterceptors(interceptorRegistry);
        }
    }

    public CorsRegistry getCorsRegistry() {
        return corsRegistry;
    }
    public InterceptorRegistry getInterceptorRegistry() {
        return interceptorRegistry;
    }

}
