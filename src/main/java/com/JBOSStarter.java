package com;
import com.application.examples.config.DataSourceConfig;
import com.application.examples.config.SqlSessionFactoryBeanConfig;
import com.application.examples.controller.UserMgrController;
import com.application.examples.pojo.User;
import com.jbosframework.boot.autoconfig.JBOSBootApplication;
import com.jbosframework.context.ApplicationContext;
import java.io.IOException;
import com.jbosframework.boot.JBOSApplication;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSessionFactory;
@JBOSBootApplication
public class JBOSStarter {

	public static void main(String[] args) throws IOException {
		JBOSApplication jbosApplication=new JBOSApplication();
		ApplicationContext ctx=jbosApplication.start(JBOSStarter.class);
		UserMgrController userMgrController=ctx.getBean("/user/getUserList",UserMgrController.class);
		userMgrController.getUserList();
//		System.out.println("******bean: "+(SqlSessionFactory)ctx.getBean("sqlSessionFactoryBean"));
	}

}
