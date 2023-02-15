package com.application.config;

import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.servlet.HandlerInterceptor;
import com.jbosframework.web.servlet.config.InterceptorRegistry;
import com.jbosframework.web.servlet.config.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/**");
    }
    public class TokenInterceptor implements HandlerInterceptor {
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            System.out.println("================TokenInterceptor");
            return true;
        }
    }
}
