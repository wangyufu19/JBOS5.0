package com.application;

import com.application.sys.service.AsyncTaskService;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
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
		AsyncTaskService asyncTaskService=ctx.getBean(AsyncTaskService.class);
		log.info("Execute async task");
		int i=1;
		while(true){
			Future<Boolean> result1=asyncTaskService.doTask1(i);
			i++;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}

		//Future<Boolean> result2=asyncTaskConfig.doTask2();



//		AspectInvoker aspectInvoker=ctx.getBean(AspectInvoker.class);
//		aspectInvoker.invoke();
    }

}
