package com.jbosframework.common.jmail.service;
import java.lang.Thread;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.jbosframework.common.jmail.message.Mail;
import com.jbosframework.common.jmail.message.MailMgr;
import com.jbosframework.common.jmail.endpoint.Endpoint;
import com.jbosframework.common.jmail.endpoint.EndpointSession;
import com.jbosframework.common.jmail.message.MailMessage;

/**
 * 邮件发送服务
 * @author youfu.wang
 * @created 2015-07-29
 */
public class MailSendService extends Thread{

	private int status=1;		
	private Endpoint endpoint;
	
	public MailSendService(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	
	public void run(){
		EndpointSession endpointSession=EndpointSession.createEndpointSession(endpoint);
		while(status==1){
			synchronized(MailMgr.sendMails){
				Mail mail=MailMgr.sendMails.poll();
				if(mail!=null){
					try {
						MailMessage mailMessage=new MailMessage(new MimeMessage(endpointSession.getSession()));		
						mailMessage.setHtmlMail(mail.isHtmlMail());
						mailMessage.setFromAddr(mail.getFromAddr());
						mailMessage.setToAddr(mail.getToAddrs());
						mailMessage.setSubject(mail.getSubject());
						mailMessage.setContent(mail.getContent());
						endpointSession.sendMessage(mailMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}		
			}		
		
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopService(){
		this.status=0;
		synchronized(MailMgr.sendMails){
			MailMgr.sendMails.clear();
		}
	}
	
	public static void main(String[] args) throws MessagingException{
		Endpoint endpoint=new Endpoint();
//		endpoint.setPop3("pop.qq.com");
//		endpoint.setSmtp("smtp.qq.com");
//		endpoint.setStorePort(995);
//		endpoint.setTransportPort(465);
//		endpoint.setEnableSSL(true);
		endpoint.setPop3("pop.163.com");
		endpoint.setSmtp("smtp.163.com");
		endpoint.setAccount("wangyufu19@163.com");
		endpoint.setPassword("Wangyufu");
//		endpoint.setAccount("124271042@qq.com");
//		endpoint.setPassword("qq_1020");
		EndpointSession endpointSession=EndpointSession.createEndpointSession(endpoint);
		MailMessage mail=new MailMessage(new MimeMessage(endpointSession.getSession()));		
		mail.setHtmlMail(true);
		mail.setFromAddr("wangyufu19@163.com");
		mail.setToAddr("124271042@qq.com");
//		mail.setFromAddr("124271042@qq.com");
//		mail.setToAddr("wangyufu19@163.com");
		mail.setSubject("测试邮件[附加附件]");
		mail.setContent("测试内容",new java.io.File("E:\\login.txt"));
		endpointSession.sendMessage(mail);
		//endpointSession.receiveMessage();
	}
	
}
