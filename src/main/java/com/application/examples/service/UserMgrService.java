package com.application.examples.service;
import com.application.examples.pojo.User;

import java.util.List;

public interface UserMgrService{
	public User getUserById(String id);
	public List getUserList();
}
