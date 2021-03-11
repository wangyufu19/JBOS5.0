package com.application;

import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.boot.JBOSApplication;
import com.application.test.aspect.AspectInvoker;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;


@JBOSBootApplication
@Slf4j
public class JBOSStarter {

	public static void main(String[] args) {
		JBOSApplication jbosApplication=new JBOSApplication(JBOSStarter.class);
		ApplicationContext ctx=jbosApplication.start(args);
    }

}
