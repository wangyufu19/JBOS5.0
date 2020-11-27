package com.jbosframework.boot.web.servlet;

import com.jbosframework.core.Order;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractFilterRegistryBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class AbstractFilterRegistryBean <T extends Filter> implements Order {

    private Map<String,String> initParameters=new HashMap<String,String>();
    private String name;
    private String urlPattern="/*";
    private int order=Order.MIN;

    public void addInitParameter(String name,String value){
        if(!initParameters.containsKey(name)){
            initParameters.put(name,value);
        }
    }
    public Map<String,String> getInitParameters(){
        return initParameters;
    }
    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return 0;
    }
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
}
