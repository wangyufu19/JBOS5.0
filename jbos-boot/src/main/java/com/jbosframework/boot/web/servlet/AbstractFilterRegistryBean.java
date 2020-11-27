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

    private Map<String,String> initParams=new HashMap<String,String>();
    private String urlPattern="/*";
    private int order=Order.MIN;

    public void addInitParam(String name,String value){
        if(!initParams.containsKey(name)){
            initParams.put(name,value);
        }
    }
    public Map<String,String> getInitParams(){
        return initParams;
    }
    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return 0;
    }
}
