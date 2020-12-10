package com.application.sys.service;

import com.application.common.UserObject;
import com.application.sys.pojo.UserInfo;

import java.util.Map;

/**
 * UserMgrService
 * @author youfu.wang
 * @date 2019-01-31
 */
public interface UserMgrService {
	/**
	 * 查询用户数据列表
	 * @param params
	 * @return
	 */
	public void getUserList(Map<String, Object> params);
	/**
	 * 根据登录名称查询用户信息
	 * @param username
	 * @return
	 */
	public Map<String, Object> getUserInfoByLoginName(String username);
	/**
	 * 根据ID查询用户信息
	 * @param userid
	 * @return
	 */
	public UserObject getUserInfoById(String userid);

	/**
	 * 更新用户信息
	 * @param user
	 */
	public void updateUserInfo(UserInfo user);
}
