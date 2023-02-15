package com.jbosframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerChain {

    HandlerExecutionChain getHandlerExecutionChain(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
