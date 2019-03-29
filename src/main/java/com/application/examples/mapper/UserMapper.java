package com.application.examples.mapper;
import java.util.List;
import java.util.Map;
/**
 * UserMapper
 * @author youfu.wang
 * @version 1.0
 */
public interface UserMapper{
	public List getUserList();
	public Map getUserById(String id);
}
