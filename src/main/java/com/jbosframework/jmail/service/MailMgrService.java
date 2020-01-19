package com.jbosframework.jmail.service;
import javax.mail.MessagingException;
import com.jbosframework.jmail.endpoint.Endpoint;
import com.jbosframework.jmail.service.MailSendService;
/**
 * 邮件管理服务
 * @author youfu.wang
 * @created 2015-07-29
 */
public class MailMgrService{
	private Endpoint endpoint;
	private MailSendService mailSendService;
	
	public MailMgrService(Endpoint endpoint){		
		this.endpoint=endpoint;
	}
	
	public void startService(){		
		//启动邮件发送服务
		mailSendService=new MailSendService(this.endpoint);
		mailSendService.start();
	}
	
	public void stopService(){
		//停止邮件发送服务
		if(mailSendService!=null)
			mailSendService.stopService();
	}
	
	public static void main(String[] args) throws MessagingException{
//		Endpoint endpoint=new Endpoint();
//		endpoint.setPop3("pop3.163.com");
//		endpoint.setSmtp("smtp.163.com");
//		endpoint.setAccount("wangyufu19@163.com");
//		endpoint.setPassword("Wangyufu");
//		MailMgrService mailMgrService=new MailMgrService(endpoint);
//		mailMgrService.startService();		
	}
}
