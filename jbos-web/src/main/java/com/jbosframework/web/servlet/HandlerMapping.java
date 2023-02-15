package com.jbosframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * HandlerMapping
 * @author youfu.wang
 * @version 5.0
 */
public interface HandlerMapping {
    void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
