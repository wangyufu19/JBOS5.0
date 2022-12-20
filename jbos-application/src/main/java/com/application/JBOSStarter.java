package com.application;

import com.application.sys.pojo.UserInfo;
import com.application.sys.service.UserMgrService;
import com.application.sys.service.impl.UserMgrServiceImpl;
import com.application.test.listener.NotifyEvent;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException, SQLException {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		ctx.publishEvent(new NotifyEvent(new Object()));
		UserMgrService userMgrService=ctx.getBean(UserMgrService.class);
		log.info("userMgrService={}",userMgrService);
        List<UserInfo> userInfos=userMgrService.getUserList(null);
        log.info("userInfos={}",userInfos);
//        AdviceConfig adviceConfig=new AdviceConfig();
//        adviceConfig.setProxyInterfaces(new Class[]{UserMgrService.class});
//        JdkDynamicProxy aopProxy=new JdkDynamicProxy(adviceConfig);
//        UserMgrService userMgrService=(UserMgrService)aopProxy.getProxy();
//        userMgrService.getUserInfoByLoginName("");

    }


}

