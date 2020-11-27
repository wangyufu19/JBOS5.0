package com.jbosframework.boot.web.servlet;

import com.jbosframework.core.Order;

import javax.servlet.http.HttpServlet;

/**
 * AbstractServletRegistryBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class AbstractServletRegistryBean  <T extends HttpServlet> implements Order {
    private String name;
    private String urlPattern="/*";
    private int order=Order.MIN;

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setUrlPattern(String urlPattern){
        this.urlPattern=urlPattern;
    }
    public String getUrlPattern(){
        return urlPattern;
    }
    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return 0;
    }
}
