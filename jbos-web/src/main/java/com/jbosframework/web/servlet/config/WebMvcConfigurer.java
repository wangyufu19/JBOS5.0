package com.jbosframework.web.servlet.config;

public interface WebMvcConfigurer {
    default void addCorsMappings(CorsRegistry registry) {
    }
    default void addInterceptors(InterceptorRegistry registry) {
    }

}
