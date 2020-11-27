package com.jbosframework.boot.web.servlet;

import com.jbosframework.beans.config.BeanPostProcessor;
import javax.servlet.ServletContext;

/**
 * FilterRegistryBeanProcessor
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-27
 */
public class WebFilterRegistryBean implements BeanPostProcessor {
    private ServletContext servletContext;

    public WebFilterRegistryBean(ServletContext servletContext){
        this.servletContext=servletContext;
    }
    public void process(Object obj){
        if(obj instanceof FilterRegistryBean){

        }
    }
}
