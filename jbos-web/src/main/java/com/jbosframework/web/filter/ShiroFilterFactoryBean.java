package com.jbosframework.web.filter;

import com.jbosframework.context.ApplicationContext;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroFilterFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public class ShiroFilterFactoryBean extends AbstractShiroFilter {
    private SecurityManager securityManager;
    private Map<String, Filter> filters=new HashMap<String, Filter>();
    private Map<String, String> filterChainMap=new LinkedHashMap<String, String>();

    public void setSecurityManager(SecurityManager securityManager){
        this.securityManager=securityManager;
    }
    public void setFilters(Map<String, Filter> filters){
        this.filters=filters;
    }
    public void setFilterChainMap(Map<String, String> filterChainMap){
        this.filterChainMap=filterChainMap;
    }

    private FilterChainManager createFilterChainManager(ServletRequest servletRequest){
        DefaultFilterChainManager manager=new DefaultFilterChainManager();
        if(this.filters!=null){
            for(Map.Entry<String,Filter> entry:this.filters.entrySet()){
                manager.addFilter(entry.getKey(),entry.getValue());
            }
        }
        if(this.filterChainMap!=null){
            for(Map.Entry<String,String> entry:this.filterChainMap.entrySet()){
                manager.createChain(entry.getKey(),entry.getValue());
            }
        }
        return manager;
    }
    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,ServletException {
        if(securityManager==null){
            throw new ServletException("The security manager does not implements the WebSecurityManager interface");
        }else{
            FilterChainManager manager=this.createFilterChainManager(servletRequest);
            PathMatchingFilterChainResolver chainResolver=new PathMatchingFilterChainResolver();
            chainResolver.setFilterChainManager(manager);
            this.setSecurityManager((WebSecurityManager)securityManager);
            this.setFilterChainResolver(chainResolver);
            super.doFilterInternal(servletRequest,servletResponse,filterChain);
        }
    }
}
