package com.application.sys.mapper;

import java.util.Map;

/**
 * UserMapper
 * @author youfu.wang
 * @date 2019-01-31
 */
public interface UserAuthMapper {
	/**
     * 用户认证
	 * @param parameterObject
     * @return
     */
	public Map<String,Object> login(Map<String, Object> parameterObject);
	/**
	 * 得到用户认证信息
	 * @param parameterObject
	 * @return
	 */
	public Map<String,Object> getUserAuthInfoById(Map<String, Object> parameterObject);
}
