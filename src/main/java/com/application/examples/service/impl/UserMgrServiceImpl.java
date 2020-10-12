package com.application.examples.service.impl;
import com.application.examples.mapper.EmpMapper;
import com.application.examples.mapper.UserMapper;
import com.application.examples.pojo.User;
import com.application.examples.service.UserMgrService;
import com.jbosframework.beans.annotation.Mapper;
import com.jbosframework.beans.annotation.Service;
import java.util.List;

@Service
public class UserMgrServiceImpl implements UserMgrService{
    @Mapper
    private UserMapper userMapper;
    @Mapper
    private EmpMapper empMapper;

    public List getUserList() {
        List list=null;
        list=userMapper.getUserList();
        List emp=empMapper.getEmpList();
        System.out.println("******user: "+list);
        System.out.println("******emp: "+emp);
        return list;
    }
    public User getUserById(String id){
        User user=null;
        user=userMapper.getUserById(id);
        return user;
    }
}
