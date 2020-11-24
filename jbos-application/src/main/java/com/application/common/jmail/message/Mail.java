package com.application.common.jmail.message;
/**
 * JMail类
 * @author youfu.wang
 * @created 2015-07-29
 */
public class Mail {
	private String messageId;
	private String fromAddr;
	private String[] toAddrs;
	private String subject;
	private String content;
	private String date;
	private int size; 
	private boolean isHtmlMail=false;
	
	public String getFromAddr() {
		return fromAddr;
	}
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}
	public String[] getToAddrs() {
		return toAddrs;
	}
	public void setToAddrs(String[] toAddrs) {
		this.toAddrs = toAddrs;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isHtmlMail() {
		return isHtmlMail;
	}
	public void setHtmlMail(boolean isHtmlMail) {
		this.isHtmlMail = isHtmlMail;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
