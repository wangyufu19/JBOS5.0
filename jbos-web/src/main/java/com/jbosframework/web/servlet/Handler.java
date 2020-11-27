package com.jbosframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler
 * @author youfu.wang
 * @version 5.0
 */
public interface Handler {
    /**
     * 执行前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception;
    /**
     * 执行后
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean postHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception;

    /**
     * 完成执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean finishHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception;
}
