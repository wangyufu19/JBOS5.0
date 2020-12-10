package com.application.sys.service.impl;
import com.application.common.UserObject;
import com.application.sys.mapper.UserMapper;
import com.application.sys.pojo.UserInfo;
import com.application.sys.service.UserMgrService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Service;
import com.jbosframework.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * UserMgrServiceImpl
 * @author youfu.wang
 * @date 2019-01-31
 */
@Service
public class UserMgrServiceImpl implements UserMgrService {

	@Autowired
	private UserMapper userMapper;
	/**
	 * 查询用户数据列表
	 * @param params
	 * @return
	 */
	public void getUserList(Map<String, Object> params) {
		return;
	}
	/**
	 * 根据登录名称查询用户信息
	 * @param username
	 * @return
	 */
	public Map<String, Object> getUserInfoByLoginName(String username){
		Map<String, Object> parameterObject=new HashMap<String, Object>();
		parameterObject.put("username",username);
		Map<String, Object> dataMap=userMapper.getUserInfoByLoginName(parameterObject);
		return dataMap;
	}
	/**
	 * 根据ID查询用户信息
	 * @param userid
	 * @return
	 */
	public UserObject getUserInfoById(String userid){
		Map<String, Object> parameterObject=new HashMap<String, Object>();
		parameterObject.put("userid",userid);
		Map<String, Object> dataMap=userMapper.getUserInfoById(parameterObject);
		if(dataMap!=null){
			UserObject userObject=new UserObject();
			userObject.setUserId(String.valueOf(dataMap.get("ID")));
			userObject.setLoginName(StringUtils.replaceNull(dataMap.get("LOGIN_NAME")));
			userObject.setUsername(StringUtils.replaceNull(dataMap.get("USER_NAME")));
			userObject.setUserStatus(StringUtils.replaceNull(dataMap.get("USER_STATUS")));
			userObject.setOrgId(StringUtils.replaceNull(dataMap.get("ORG_ID")));
			userObject.setDepId(StringUtils.replaceNull(dataMap.get("DEP_ID")));
			return userObject;
		}else{
			return null;
		}
	}
	/**
	 * 更新用户信息
	 * @param user
	 */
	//@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	public void updateUserInfo(UserInfo user){

	}
}
