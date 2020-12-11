package com.application.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * AuthToken
 * @author youfu.wang
 * @version 5.0
 */
public class AuthToken implements AuthenticationToken {
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}