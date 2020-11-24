package com.application.common.jmail.endpoint;
/**
 * 终端参数
 * @author youfu.wang
 * @created 2015-07-29
 */
public class Endpoint {
	private String charset="gbk";
	private String account;
	private String password;
	private String pop3;
	private String smtp;
	private int storePort=110;
	private int transportPort=25;
	private String storeProtocol="pop3";
	private String transportProtocol="smtp";
	private boolean enableSSL=false;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStorePort() {
		return storePort;
	}
	public void setStorePort(int storePort) {	
		this.storePort = storePort;
	}
	public String getStoreProtocol() {
		return storeProtocol;
	}
	public void setStoreProtocol(String storeProtocol) {
		this.storeProtocol = storeProtocol;
	}
	public int getTransportPort() {
		return transportPort;
	}
	public void setTransportPort(int transportPort) {
		this.transportPort = transportPort;
	}
	public String getTransportProtocol() {
		return transportProtocol;
	}
	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPop3() {
		return pop3;
	}
	public void setPop3(String pop3) {
		this.pop3 = pop3;
	}
	public String getSmtp() {
		return smtp;
	}
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public boolean isEnableSSL() {
		return enableSSL;
	}
	public void setEnableSSL(boolean enableSSL) {
		this.enableSSL = enableSSL;
	}
	
}
