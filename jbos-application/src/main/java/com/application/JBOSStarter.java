package com.application;

import com.application.config.AsyncTaskConfig;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.application.test.aspect.AspectInvoker;
import com.jbosframework.schedule.annotation.EnableAsync;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;


@JBOSBootApplication
@EnableAsync
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
		AsyncTaskConfig asyncTaskConfig=ctx.getBean(AsyncTaskConfig.class);
		log.info("Execute async task");
		int i=1;
		while(true){
			Future<Boolean> result1=asyncTaskConfig.doTask1(i);
			i++;
		}

		//Future<Boolean> result2=asyncTaskConfig.doTask2();



//		AspectInvoker aspectInvoker=ctx.getBean(AspectInvoker.class);
//		aspectInvoker.invoke();
    }

}
