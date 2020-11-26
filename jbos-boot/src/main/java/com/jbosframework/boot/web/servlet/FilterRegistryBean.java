package com.jbosframework.boot.web.servlet;

import javax.servlet.Filter;

/**
 * FilterRegistryBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class FilterRegistryBean<T extends Filter> extends AbstractFilterRegistryBean{
    private T filter;

    public FilterRegistryBean(){

    }
    public void setFilter(T filter){
        this.filter=filter;
    }
    public T getFilter(){
        return filter;
    }

}
