package com.jbosframework.net.sftp;
import java.util.Properties;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * SftpChannelClient
 * @author youfu.wang
 * @version 1.0
 * @date 2017-10-10
 */
public class SftpClient {
	protected Log logger=LogFactory.getLog(this.getClass());	
	public static final int SFTP_DEFAULT_TIMEOUT=60000;
	public static final int SFTP_DEFAULT_PORT=22;
	private String host;
	private String username;
	private String password;
	private int port;
	private Session session=null;
	private Channel channel=null;
	
	/**
	 * 构造方法
	 * @param host
	 * @param username
	 * @param password
	 */
	public SftpClient(String host,String username,String password){
		this.host=host;
		this.username=username;
		this.password=password;
		this.port=SFTP_DEFAULT_PORT;
	}
	/**
	 * 构造方法
	 * @param host
	 * @param username
	 * @param password
	 * @param port
	 */
	public SftpClient(String host,String username,String password,int port){
		this.host=host;
		this.username=username;
		this.password=password;
		this.port=port;
	}
	/**
	 * 打开Sftp会话
	 * @throws JSchException 
	 */
	public ChannelSftp openChannel() throws JSchException{		
		return this.openChannel(SFTP_DEFAULT_TIMEOUT);
	}
	/**
	 * 打开Sftp会话
	 * @throws JSchException 
	 */
	public ChannelSftp openChannel(int timeout) throws JSchException{		
		JSch jsch = new JSch();
		session=jsch.getSession(username,host,port);
		logger.info("******创建["+host+"]主机SFTP会话");
		if(session!=null){
			session.setPassword(password);
			Properties config = new Properties();
	        config.put("StrictHostKeyChecking", "no");
	        //为Session对象设置properties
	        session.setConfig(config); 
			session.setTimeout(timeout);
	        session.connect();
		}	
        logger.info("******连接["+host+"]主机SFTP会话");
        channel=session.openChannel("sftp");
        if(channel!=null){
        	 channel.connect();   
        }       
        logger.info("******打开["+host+"]主机SFTP通道");
        return (ChannelSftp)channel;
	}
	/**
	 * 关闭Sftp会话
	 */
	public void closeChannel(){
		if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
        logger.info("******关闭["+host+"]主机SFTP会话");
	}
	public static void main(String[] args) throws JSchException{
		SftpClient sftpClient=new SftpClient(
				"192.168.56.130",
				"root",
				"123456");
		sftpClient.openChannel();
		sftpClient.closeChannel();
	}
}
