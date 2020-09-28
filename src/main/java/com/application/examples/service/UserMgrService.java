package com.application.examples.service;
import com.application.examples.pojo.User;
import com.jbosframework.beans.annotation.Service;

import java.util.List;

public interface UserMgrService{
	public User getUserById(String id);
	public List getUserList();
}
