package com.application.sys.service.impl;

import com.application.common.utils.TokenGenerator;
import com.application.sys.mapper.UserTokenMapper;
import com.application.sys.pojo.UserToken;
import com.application.sys.service.UserMgrService;
import com.application.sys.service.UserTokenService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.orm.mybatis.annotation.Mapper;
import com.jbosframework.beans.annotation.Service;
import com.jbosframework.transaction.annotation.Isolation;
import com.jbosframework.transaction.annotation.Propagation;
import com.jbosframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UserTokenServiceImpl
 * @author youfu.wang
 */
@Service
public class UserTokenServiceImpl  implements UserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;
    @Autowired
    private UserMgrService userMgrService;
    @Mapper
    private UserTokenMapper userTokenMapper;
    @Autowired
    private DataSource dataSource;
    /**
     * 创建用户Token
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public String createToken(String userId){
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //查询用户Token数据
        UserToken userToken=getUserTokenByUserId(userId);
        //生成一个Token
        String token = TokenGenerator.generateValue();
        if(null==userToken){
            userToken=new UserToken();
            userToken.setUserId(userId);
            userToken.setToken(token);
            userToken.setExpireTime(expireTime);
            userToken.setUpdateTime(now);
            userTokenMapper.addUserToken(userToken);
        }else{
            //更新Token过期时间
            userToken.setUserId(userId);
            userToken.setToken(token);
            userToken.setExpireTime(expireTime);
            userToken.setUpdateTime(now);
            userTokenMapper.updateUserToken(userToken);
            token= userToken.getToken();
        }
        return token;

    }
    /**
     * 失效用户Token
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void invalidToken(String userId){
        //查询用户Token数据
        UserToken userToken=getUserTokenByUserId(userId);
        if(userToken==null){
            return;
        }else{
            //当前时间
            Date now = new Date();
            //更新Token过期时间
            userToken.setExpireTime(now);
            userToken.setUpdateTime(now);
            userTokenMapper.updateUserToken(userToken);
        }
    }
    /**
     * 查询ID用户Token
     * @param userId
     * @return
     */
    public UserToken getUserTokenByUserId(String userId){
        Map<String, Object> parameterObject=new HashMap<String, Object>();
        parameterObject.put("userId",userId);
        UserToken userToken=userTokenMapper.getUserTokenByUserId(parameterObject);
        return userToken;
    }
    /**
     * 根据accessToken查询用户Token
     * @param accessToken
     * @return
     */
    public UserToken getUserTokenByAccessToken(String accessToken){
        Map<String, Object> parameterObject=new HashMap<String, Object>();
        parameterObject.put("token",accessToken);
        UserToken userToken=userTokenMapper.getUserTokenByAccessToken(parameterObject);
        return userToken;
    }
}
