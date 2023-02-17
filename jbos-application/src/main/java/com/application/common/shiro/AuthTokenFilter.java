package com.application.common.shiro;

import com.application.common.utils.Return;
import com.jbosframework.web.mvc.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jbosframework.utils.JsonUtils;
/**
 * AuthTokenFilter
 * @author youfu.wang
 * @date 2020-12-09
 */
@Slf4j
public class AuthTokenFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token=this.getRequestToken((HttpServletRequest)servletRequest);
        log.info("******createToken[token="+token+"]");
        if(StringUtils.isBlank(token)){
            return null;
        }
        return new AuthToken(token);
    }
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("*******isAccessAllowed");
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        return false;
    }
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        log.info("******onAccessDenied");
        //获取请求token，如果token不存在，返回401
        String token = getRequestToken((HttpServletRequest) servletRequest);
        if(StringUtils.isBlank(token)){
            HttpServletRequest request=(HttpServletRequest) servletRequest;
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            httpResponse.setContentType("application/json;charset=UTF-8");
            String json =JsonUtils.toJson(Return.error(HttpStatus.SC_UNAUTHORIZED+"", "invalid token"));
            httpResponse.getWriter().print(json);
            return false;
        }
        return executeLogin(servletRequest, servletResponse);
    }
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        try {
            log.error(e.getMessage(),e);
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Return r = Return.error(HttpStatus.SC_UNAUTHORIZED+"", throwable.getMessage());
            String json = JsonUtils.toJson(r);
            httpResponse.getWriter().print(json);
        } catch (Exception e1) {
            log.error(e1.getMessage(),e1);
        }
        return false;
    }
    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest request){
        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }
}
