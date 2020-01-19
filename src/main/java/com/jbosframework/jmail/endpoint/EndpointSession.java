package com.jbosframework.jmail.endpoint;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;

import com.jbosframework.jmail.message.Mail;
import com.jbosframework.jmail.message.MailMessage;
import com.jbosframework.utils.DateUtils;
import com.jbosframework.utils.StringUtils;

/**
 * 终端会话认证
 * @author youfu.wang
 * @created 2015-07-29
 */
public class EndpointSession {
	private Session session;
	private Endpoint endpoint;
	
	public EndpointSession(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	public static EndpointSession createEndpointSession(Endpoint endpoint){
		EndpointSession endpointSession=new EndpointSession(endpoint);
		endpointSession.openSession();
		return endpointSession;
	}
	private void openSession(){		
		Properties props = new Properties();           
        props.put("mail.pop3", StringUtils.replaceNull(this.endpoint.getPop3()));      
        props.put("mail.smtp", StringUtils.replaceNull(this.endpoint.getSmtp()));
        props.put("mail.smtp.auth", "true");       
        props.put("mail.store.protocol", StringUtils.replaceNull(this.endpoint.getStoreProtocol()));
        props.put("mail.transport.protocol", StringUtils.replaceNull(this.endpoint.getTransportProtocol()));
        props.put("mail.mime.charset",this.endpoint.getCharset());
        if(endpoint.isEnableSSL()){
        	props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        	props.put("mail.pop3.socketFactory.fallback", "false");  
        	props.put("mail.pop3.socketFactory.port",String.valueOf(this.endpoint.getStorePort()));  
        	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        	props.put("mail.smtp.socketFactory.port",String.valueOf(this.endpoint.getTransportPort()));        	 
        	props.put("mail.smtp.starttls.enable","true");        
        }
        session = Session.getDefaultInstance(props,new Authenticator(){
        	public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(endpoint.getAccount(), endpoint.getPassword()); 
            }
        });  
        session.setDebug(false);
	}
	public Session getSession(){
		return this.session;
	}
	public void sendMessage(MailMessage mailMessage) throws MessagingException{	
		Message message=mailMessage.getMessage();
		message.setSentDate(DateUtils.getCurrentDate());
		message.saveChanges();
        Transport trans = null;    	
        trans = getTransport();    
        trans.sendMessage(message,message.getAllRecipients());   
        trans.close();       
	}
	public List<Mail> receiveMessage() throws MessagingException{
		List<Mail> mails=new ArrayList<Mail>();
		Store store =null;     		    		
		store=this.getStore();      		
		store.connect();    	    		
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);    		
		Message[] messages =folder.getMessages();		
//		Flags flags = new Flags(Flags.Flag.DELETED); //删除标记   	
		for(int i = 0, n = messages.length; i < n; i++){    
			MailMessage mailMessage=new MailMessage(messages[i]);  
			Mail mail=new Mail();
			mail.setMessageId(mailMessage.getMessageId());
			mail.setFromAddr(mailMessage.getSendForm());
			String[] toAddrs=new String[1];
			toAddrs[0]=mailMessage.getSendTo();
			mail.setToAddrs(toAddrs);
			mail.setSubject(mailMessage.getSubject());
			mail.setContent(mailMessage.getContent());
			mail.setDate(mailMessage.getSendDate());
			mail.setSize(mailMessage.getSize());
			System.out.println("******messageNo="+mail.getMessageId());
 			System.out.println("******sender="+mail.getFromAddr());
 			System.out.println("******receiver="+mail.getToAddrs());
 			System.out.println("******subject="+mail.getSubject());
 			System.out.println("******recTime="+mail.getDate());
 			System.out.println("******content="+mail.getContent()); 			
 			System.out.println("******capacity="+mail.getSize());
			mails.add(mail);
//			if(i>=1) break;
// 			message[i].setFlags(flags, true); 			
		}
		folder.close(false);
		store.close();
		return mails;
	}
	private Transport getTransport() throws MessagingException {    	
        //发送邮件需要服务器验证 
        URLName urlName = new URLName(session.getProperty("mail.transport.protocol"),
                                      session.getProperty("mail.smtp"), 
                                      this.endpoint.getTransportPort(), 
                                      null, 
                                      this.endpoint.getAccount(), 
                                      this.endpoint.getPassword());
        Transport trans = session.getTransport(urlName);    
        trans.connect();      
        return trans;
    }
	private Store getStore()throws MessagingException{
   	 /* 接收邮件需要服务器验证 */
       URLName urlName = new URLName(session.getProperty("mail.store.protocol"),
                                     session.getProperty("mail.pop3"),
                                     this.endpoint.getStorePort(),
                                     null,
                                     this.endpoint.getAccount(),
                                     this.endpoint.getPassword());
       Store store = session.getStore(urlName);
       return store;
	}
	
}
