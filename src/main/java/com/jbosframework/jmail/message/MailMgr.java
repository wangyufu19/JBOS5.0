package com.jbosframework.jmail.message;
import java.util.concurrent.LinkedBlockingQueue;
import com.jbosframework.jmail.message.Mail;

/**
 * 邮件管理类
 * @author youfu.wang
 * @created 2015-07-29
 */
public class MailMgr {
	public static LinkedBlockingQueue<Mail> sendMails=new LinkedBlockingQueue<Mail>();
	public static LinkedBlockingQueue<Mail> receiveMails=new LinkedBlockingQueue<Mail>();
	/**
	 * 新增邮件到发送队列
	 * @param mail
	 */
	public void addSendMail(Mail mail){		
		synchronized(MailMgr.sendMails){
			try {
				MailMgr.sendMails.put(mail);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 新增邮件到接收队列
	 * @param mail
	 */
	public void addReceiveMail(Mail mail){
		synchronized(MailMgr.receiveMails){
			try {
				MailMgr.receiveMails.put(mail);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		String[] toAddrs=new String[1];
		toAddrs[0]="124271042@qq.com";
		Mail mail=new Mail();
		mail.setHtmlMail(true);
		mail.setFromAddr("wangyufu19@163.com");
		mail.setToAddrs(toAddrs);
		mail.setSubject("测试邮件");
		mail.setContent("测试内容");
		MailMgr mailQueueMgr=new MailMgr();
		mailQueueMgr.addSendMail(mail);
	}

}
