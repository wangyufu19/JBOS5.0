package com.jbosframework.web.servlet.config;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.PathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorsRegistration {
    private static final String DEFAULT_PATH_PATTERN="/*";
    private final String pathPattern;
    private final List<String> allowedOrigins = new ArrayList();
    private boolean allowCredentials=true;
    private final List<String> allowedMethods = new ArrayList();
    private int maxAge=3600;
    @Nullable
    private PathMatcher pathMatcher;
    private int order = 0;

    public CorsRegistration(String pathPattern){
        this.pathPattern=pathPattern;
    }
    public CorsRegistration allowedOrigins(String... allowedOrigins) {
        return this.allowedOrigins(Arrays.asList(allowedOrigins));
    }
    public CorsRegistration allowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins.addAll(allowedOrigins);
        return this;
    }
    public CorsRegistration allowCredentials(boolean allowCredentials){
        this.allowCredentials=allowCredentials;
        return this;
    }
    public CorsRegistration allowedMethods(String... allowedMethods) {
        return this.allowedMethods(Arrays.asList(allowedMethods));
    }
    public CorsRegistration allowedMethods(List<String> allowedMethods) {
        this.allowedMethods.addAll(allowedMethods);
        return this;
    }
    public String getAllowedOrigins(){
        return String.join(",",allowedOrigins);
    }
    public String getAllowedMethods(){
        return String.join(",",allowedMethods);
    }
    public String getAllowCredentials(){
        return String.valueOf(this.allowCredentials);
    }
    public CorsRegistration maxAge(int maxAge){
        this.maxAge=maxAge;
        return this;
    }
    public String getMaxAge(){
        return String.valueOf(this.maxAge);
    }
    public CorsRegistration pathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
        return this;
    }
    public boolean isMatch(String requestUri){
        if(this.pathMatcher.match(pathPattern,requestUri)){
            return true;
        }
        return false;
    }
    public CorsRegistration order(int order) {
        this.order = order;
        return this;
    }
    public int getOrder() {
        return this.order;
    }
}
