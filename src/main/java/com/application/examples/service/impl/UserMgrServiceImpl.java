package com.application.examples.service.impl;
import com.application.examples.mapper.UserMapper;
import com.application.examples.pojo.User;
import com.application.examples.service.UserMgrService;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Service;
import java.util.List;

@Service("userMgrService")
public class UserMgrServiceImpl implements UserMgrService{
    @Autowired
    private UserMapper userMapper;

    public List getUserList() {
        List list=null;
        list=userMapper.getUserList();
        System.out.println("******list: "+list);
        return list;
    }
    public User getUserById(String id){
        User user=null;
        user=userMapper.getUserById(id);
        return user;
    }
}
