package com.application.sys.service;

import com.application.sys.pojo.UserToken;

/**
 * UserTokenService
 * @author youfu.wang
 */
public interface UserTokenService {
    /**
     * 创建用户Token
     * @param userid
     */
    public String createToken(String userid);
    /**
     * 失效用户Token
     * @param userid
     */
    public void invalidToken(String userid);
    /**
     * 查询ID用户Token
     * @param userid
     * @return
     */
    public UserToken getUserTokenByUserId(String userid);
    /**
     * 查询用户Token
     * @param accessToken
     * @return
     */
    public UserToken getUserTokenByAccessToken(String accessToken);

}
