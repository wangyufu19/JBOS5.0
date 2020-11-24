package com.application.examples.mapper;
import com.application.examples.pojo.User;

import java.util.List;
import java.util.Map;
/**
 * UserMapper
 * @author youfu.wang
 * @version 1.0
 */
public interface UserMapper{
	public List getUserList();
	public User getUserById(String id);
}
