package com.jbosframework.web.servlet.config;


import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.PathMatcher;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterceptorRegistration {
    private final HandlerInterceptor interceptor;
    private final List<String> includePatterns = new ArrayList();
    private final List<String> excludePatterns = new ArrayList();
    @Nullable
    private PathMatcher pathMatcher;
    private int order = 0;

    public InterceptorRegistration(HandlerInterceptor interceptor) {
        Assert.notNull(interceptor, "Interceptor is required");
        this.interceptor = interceptor;
    }


    public InterceptorRegistration addPathPatterns(String... patterns) {
        return this.addPathPatterns(Arrays.asList(patterns));
    }
    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }
    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return this.excludePathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration pathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        return this;
    }
    public boolean isMatch(String requestUri){
        for(String excludePattern:excludePatterns){
            if(this.pathMatcher.match(excludePattern,requestUri)){
                return false;
            }
        }
        for(String includePattern:includePatterns){
            if(this.pathMatcher.match(includePattern,requestUri)){
                return true;
            }
        }
        return false;
    }
    public InterceptorRegistration order(int order) {
        this.order = order;
        return this;
    }
    public int getOrder() {
        return this.order;
    }

    public HandlerInterceptor getInterceptor() {
        return this.interceptor;
    }

}
