package com.jbosframework.web.http;

import com.jbosframework.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServletHttpServletRequest extends HttpServletRequestWrapper {

    private Map<String,String> headers=new ConcurrentHashMap(128);

    public ServletHttpServletRequest(HttpServletRequest request){
        super(request);
    }
    public void setHeader(String name,String value){
        this.headers.put(name,value);
    }
    public String getHeader(String name){
        String value=this.getHeader(name);
        if(StringUtils.isNUll(value)){
            value=headers.get(name);
        }
        return value;
    }

}
