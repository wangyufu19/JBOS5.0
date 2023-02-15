package com.jbosframework.web.servlet.config;
import java.util.Iterator;
import java.util.List;


public class WebMvcConfigureDelegate {
    private List<WebMvcConfigurer> configurers;

    public WebMvcConfigureDelegate(List<WebMvcConfigurer> configurers){
        this.configurers=configurers;
    }
    public void addInterceptors(InterceptorRegistry registry) {
        for(Iterator<WebMvcConfigurer> it=configurers.iterator();it.hasNext();){
            WebMvcConfigurer webMvcConfigurer=it.next();
            webMvcConfigurer.addInterceptors(registry);
        }
    }


}
