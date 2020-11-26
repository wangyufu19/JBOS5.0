package com.jbosframework.web.filter;

import com.jbosframework.web.context.Charset;

import javax.servlet.*;
import java.io.IOException;

/**
 * AbstractFilterBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public abstract class AbstractFilterBean implements Filter {
    private String encoding= Charset.UTF8;
    protected boolean ignore = false;
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if("true".equals(value)){
            this.ignore=true;
        }else{
            this.ignore=false;
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilterInternal(servletRequest,servletResponse,filterChain);
    }
    public abstract void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException;

    public void destroy() {
        filterConfig=null;
        ignore = false;
    }
    public String getEncoding(){
        return encoding;
    }
    public boolean isIgnoreEncoding(){
        return ignore;
    }
}
