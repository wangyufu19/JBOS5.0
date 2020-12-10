package com.application.examples.config;

import com.application.examples.shiro.AuthRealm;
import com.application.examples.shiro.AuthTokenFilter;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.filter.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig
 * @author youfu.wang
 * @version 5.0
 */
@Configuration
public class ShiroConfig {

    @Bean
    public SecurityManager securityManager(AuthRealm authRealm) {
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(authRealm);
        return securityManager;
    }
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters=new HashMap<String, Filter>();
        filters.put("authToken", new AuthTokenFilter());
        shiroFilterFactoryBean.setFilters(filters);
        Map<String, String> filterChainMap=new LinkedHashMap<String, String>();
        filterChainMap.put("/user/getUserList","anon");
        filterChainMap.put("/**","authToken");
        shiroFilterFactoryBean.setFilterChainMap(filterChainMap);
        return shiroFilterFactoryBean;
    }
}
