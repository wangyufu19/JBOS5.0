package com.jbosframework.web.servlet.config;

public interface WebMvcConfigurer {
    default void addInterceptors(InterceptorRegistry registry) {
    }

}
