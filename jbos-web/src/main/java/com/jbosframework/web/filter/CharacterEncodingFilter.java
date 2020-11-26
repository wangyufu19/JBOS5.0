package com.jbosframework.web.filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 * CharacterEncodingFilter
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-26
 */
public class CharacterEncodingFilter extends AbstractFilterBean{

    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest.getCharacterEncoding() == null){
            String encoding=this.getEncoding();
            if(encoding!=null){
                servletRequest.setCharacterEncoding(encoding);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
