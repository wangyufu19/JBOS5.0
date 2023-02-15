package com.jbosframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    HandlerExecutionChain getHandlerExecutionChain(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
