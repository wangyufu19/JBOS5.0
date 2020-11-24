package com.jbosframework.net.sftp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * SftpCommand
 * @author youfu.wang
 * @version 1.0
 * @date 2017-10-10
 */
public class SftpCommand {
	protected Log logger=LogFactory.getLog(this.getClass());	
	private ChannelSftp channelSftp=null;
	/**
	 * 构造方法
	 * @param channelSftp
	 */
	public SftpCommand(ChannelSftp channelSftp){
		this.channelSftp=channelSftp;
	}
	/**
	 * 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
	 * 采用默认的传输模式：OVERWRITE
	 * @param src
	 * @param dst
	 * @throws SftpException
	 */
	public void put(String src,String dst) throws SftpException{
		if(this.channelSftp!=null){
			this.channelSftp.put(src, dst);
		}		
	}
	/**
	 * 将目标服务器为src的文件下传到本地，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
	 * 采用默认的传输模式：OVERWRITE
	 * @param src
	 * @param dst
	 * @throws SftpException
	 */
	public void get(String src,String dst) throws SftpException{
		if(this.channelSftp!=null){
			this.channelSftp.get(src, dst);
		}
	}
	/**
	 * 列出目标目录所有子目录和文件
	 * @param path
	 * @return
	 * @throws SftpException
	 */
	public List<LsEntry> list(String path) throws SftpException{
		List<LsEntry> fileList=null;
		Vector v=null;
		if(this.channelSftp!=null){
			v=this.channelSftp.ls(path);
			if(v!=null){
				fileList=new ArrayList<LsEntry>();
				for(int i=0;i<v.size();i++){
					LsEntry lsEntry=(LsEntry)v.get(i);
					fileList.add(lsEntry);
				}
			}			
		}		
		return fileList;
	}
	public static void main(String[] args) throws JSchException, SftpException{
		SftpClient sftpClient=new SftpClient(
				"192.168.56.130",
				"root",
				"123456");
		ChannelSftp channelSftp=sftpClient.openChannel();
		SftpCommand sftpCommand=new SftpCommand(channelSftp);
		//sftpCommand.put("E:\\login.txt", "/home/sftp");
		sftpCommand.get("/home/sftp/login.txt","e://");
		channelSftp.quit();
		sftpClient.closeChannel();
	}
}
