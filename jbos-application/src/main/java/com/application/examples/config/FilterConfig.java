package com.application.examples.config;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.web.servlet.FilterRegistryBean;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.filter.CharacterEncodingFilter;
import com.jbosframework.web.filter.DelegatingFilterProxy;

/**
 * FilterConfig
 * @author youfu.wang
 * @data 2020-12-27
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistryBean shiroFilterRegistry(){
        FilterRegistryBean filterRegistryBean=new FilterRegistryBean();
        filterRegistryBean.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistryBean.setUrlPattern("/*");
        return filterRegistryBean;
    }
    //@Bean
    public FilterRegistryBean characterEncodingFilter(){
        FilterRegistryBean filterRegistryBean=new FilterRegistryBean();
        filterRegistryBean.setFilter(new CharacterEncodingFilter());
        filterRegistryBean.addInitParameter("encoding","UTF-8");
        filterRegistryBean.setName("characterEncodingFilter");
        filterRegistryBean.setUrlPattern("/*");
        return filterRegistryBean;
    }
}
