package com.jbosframework.web.servlet.config;
import java.util.Iterator;
import java.util.List;


public class WebMvcConfigureDelegate {
    private InterceptorRegistry registry=new InterceptorRegistry();

    public void addInterceptors(List<WebMvcConfigurer> configurers) {
        for(Iterator<WebMvcConfigurer> it=configurers.iterator();it.hasNext();){
            WebMvcConfigurer webMvcConfigurer=it.next();
            webMvcConfigurer.addInterceptors(registry);
        }
    }
    public InterceptorRegistry getInterceptorRegistry() {
        return registry;
    }
}
