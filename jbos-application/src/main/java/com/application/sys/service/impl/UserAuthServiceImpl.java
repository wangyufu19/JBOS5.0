package com.application.sys.service.impl;
import com.application.sys.mapper.UserAuthMapper;
import com.application.sys.service.UserAuthService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Mapper;
import com.jbosframework.beans.annotation.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证服务类
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Mapper
    private UserAuthMapper userAuthMapper;
    /**
     * 用户认证
     * @param username
     * @return
     */
    public Map<String,Object> login(String username){
        Map<String, Object> parameterObject=new HashMap<String, Object>();
        parameterObject.put("username",username);
        return userAuthMapper.login(parameterObject);
    }
    /**
     * 得到用户认证信息
     * @param userid
     * @return
     */
    public Map<String,Object> getUserAuthInfoById(String userid){
        Map<String, Object> parameterObject=new HashMap<String, Object>();
        parameterObject.put("userid",userid);
        return userAuthMapper.getUserAuthInfoById(parameterObject);
    }
}
