package com.application.common.jmail.message;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.jbosframework.web.utils.WebUtils;
import com.jbosframework.utils.DateUtils;
/**
 * JMail消息类
 * @author youfu.wang
 * @created 2015-07-29
 */
public class MailMessage {
	private Message message;
	private String charset="gbk";
	private boolean isHtmlMail=false;	

	public MailMessage(Message message){
		this.message=message;
	}
	public Message getMessage(){
		return this.message;
	}
	
	public String getMessageId(){
    	String msgId="";    	
    	try {    		
			msgId=((MimeMessage)message).getMessageID();
		} catch (MessagingException e) {		
			e.printStackTrace();
		}
    	return msgId;
    }
	public String getSendForm(){
	    String formAddr="";
		try {
			formAddr=((InternetAddress) message.getFrom()[0]).toString();
			formAddr= WebUtils.decode(formAddr, charset);
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
		try {
			formAddr=MimeUtility.decodeText(formAddr);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return formAddr;
	}
	public String getSendTo(){
    	String toAddr="";
    	try {
			toAddr=((InternetAddress) message.getAllRecipients()[0]).toString();
			toAddr= WebUtils.decode(toAddr, charset);
		} catch (MessagingException e) {		
			e.printStackTrace();
		}
		try {
			toAddr=MimeUtility.decodeText(toAddr);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
    	return toAddr;
    }
	public String getSubject(){
    	String subject="";
    	try {    		
			subject=message.getSubject();
			if(subject==null)
				subject="";
			//subject=WebCodeUtils.decode(subject, charset);
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
		try {
			subject=MimeUtility.decodeText(subject);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
    	return subject;
    }
	public String getContent(){
    	String content="";
    	Object obj;
		try {
			obj = message.getContent();
			if(obj instanceof MimeMultipart){
				MimeMultipart mp=(MimeMultipart)obj;
				int count=mp.getCount();
				for(int j=0;j<count;j++){
					Part part  = mp.getBodyPart(j);    
					String disposition = part.getDisposition();    					
					if(disposition!=null&&((disposition.equals(part.ATTACHMENT)||disposition.equals(part.INLINE)))){
						//接收附件
					}else if(disposition==null){
				        //接收text/plain,image/gif,text/html
						MimeBodyPart mbp = (MimeBodyPart)part;						
						if(mbp.isMimeType("text/html")) {
							content=mbp.getContent().toString();								
		    			}    		    				
					}    								
				}
			}else{
				content=String.valueOf(obj);
			}
			try {
				content=MimeUtility.decodeText(content);
			} catch (UnsupportedEncodingException e) {			
				e.printStackTrace();
			}
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (MessagingException e) {			
			e.printStackTrace();
		}    			
		return content;
    }
	 public String getSendDate(){
    	String senddate=null;
    	try {    		
			senddate=DateUtils.format(message.getSentDate());
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
    	return senddate;
    }
	public int getSize(){
    	int size=0;;
    	try {
			size=message.getSize();
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
    	return size;
    }
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isHtmlMail() {
		return isHtmlMail;
	}
	public void setHtmlMail(boolean isHtmlMail) {
		this.isHtmlMail = isHtmlMail;
	}
	public void setFromAddr(String fromAddr) throws MessagingException{
		Address addr = new InternetAddress(fromAddr);
        message.setFrom(addr);
	}
	public void setToAddr(String toAddr) throws MessagingException{
		Address addr = new InternetAddress(toAddr);
	    message.setRecipient(Message.RecipientType.TO, addr);
	}
	public void setToAddr(String[] toAddrs) throws MessagingException {
        Address[] addrs = new Address[toAddrs.length];
        for(int i=0; i<toAddrs.length; i++) {
            addrs[i] = new InternetAddress(toAddrs[i]);
        }
        message.setRecipients(Message.RecipientType.TO, addrs);
    }
	public void setSendCC(String ccAddr) throws MessagingException {
	    Address addr = new InternetAddress(ccAddr);
	    message.addRecipient(Message.RecipientType.CC, addr);
	}	    
    public void setSendBCC(String bccAddr) throws MessagingException {
        Address addr = new InternetAddress(bccAddr);
        message.addRecipient(Message.RecipientType.BCC, addr);
    }
	public void setSubject(String subject) throws MessagingException{
		 message.setSubject(subject);
	}
	public void setContent(String content) throws MessagingException {
        if(content != null &&!"".equals(content)) {
        	//添加邮件正文
        	Multipart multipart = new MimeMultipart();
        	BodyPart contentPart = new MimeBodyPart();
        	contentPart.setContent(content, "text/html;charset=" + charset);
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
        }
    }
	public void setContent(String content,File attachment) throws MessagingException{
		 if(content != null &&!"".equals(content)) {
        	Multipart multipart = new MimeMultipart();
        	//添加邮件正文
        	BodyPart contentPart = new MimeBodyPart();
        	contentPart.setContent(content, "text/html;charset=" + charset);
            multipart.addBodyPart(contentPart);
            //添加邮件附件
            if(attachment!=null){
            	BodyPart attachmentBodyPart = new MimeBodyPart();
            	DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                try {
					attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                multipart.addBodyPart(attachmentBodyPart);
            }
            message.setContent(multipart);
        }
    }
}
