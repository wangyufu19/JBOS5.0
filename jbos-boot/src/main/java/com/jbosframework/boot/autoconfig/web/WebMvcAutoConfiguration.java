package com.jbosframework.boot.autoconfig.web;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextAware;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.Import;
import com.jbosframework.web.servlet.config.InterceptorRegistry;
import com.jbosframework.web.servlet.config.WebMvcConfigureDelegate;
import com.jbosframework.web.servlet.config.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import(WebMvcRegister.class)
public class WebMvcAutoConfiguration{

    @Configuration
    public static class WebMvcConfigurationAdapter implements ApplicationContextAware {
        private ApplicationContext applicationContext;
        @Autowired
        private List<WebMvcConfigurer> configurers=new ArrayList<>();

        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext=applicationContext;
        }
        @Bean
        public WebMvcConfigureDelegate webMvcConfigureDelegate(){
            InterceptorRegistry registry=new InterceptorRegistry();
            WebMvcConfigureDelegate delegate=new WebMvcConfigureDelegate(this.configurers);
            delegate.addInterceptors(registry);
            return delegate;
        }
    }
}
