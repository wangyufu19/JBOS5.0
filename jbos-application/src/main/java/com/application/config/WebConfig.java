package com.application.config;

import com.application.common.utils.JacksonUtils;
import com.application.common.utils.Return;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.web.servlet.HandlerInterceptor;
import com.jbosframework.web.servlet.config.InterceptorRegistry;
import com.jbosframework.web.servlet.config.WebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/**");
    }
    public class TokenInterceptor implements HandlerInterceptor {
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            log.info("uri={}",request.getRequestURI());
            String accessToken = getRequestToken(request);
            if(accessToken == null) {
                Return r = Return.error("token失效或认证过期！");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(JacksonUtils.toJson(r));
                out.flush();
                out.close();
                return false;
            }
            return true;
        }
        private String getRequestToken(HttpServletRequest request) {
            String accessToken = request.getHeader("accessToken");
            if (accessToken == null) {
                return request.getParameter("accessToken");
            } else {
                return accessToken;
            }
        }
    }
}
