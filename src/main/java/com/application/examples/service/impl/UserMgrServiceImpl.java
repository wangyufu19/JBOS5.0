package com.application.examples.service.impl;
import com.application.examples.mapper.UserMapper;
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
//        try {
//            userMapper=this.openSession().getMapper(com.application.examples.mapper.UserMapper.class);
//            userMapper=null;
//            list=userMapper.getUserList();
//        }catch(Exception e) {
//            e.printStackTrace();
//        }finally {
//            this.closeSession();
//        }
        return list;
    }
}
