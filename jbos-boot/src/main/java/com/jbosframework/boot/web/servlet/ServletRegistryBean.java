package com.jbosframework.boot.web.servlet;

import javax.servlet.http.HttpServlet;

/**
 * ServletRegistryBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class ServletRegistryBean <T extends HttpServlet> extends AbstractServletRegistryBean{
    private T servlet;

    public ServletRegistryBean(){

    }
    public void setServlet(T servlet){
        this.servlet=servlet;
    }
    public T getServlet(){
        return servlet;
    }
}
