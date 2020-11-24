package com.jbosframework.net.socket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/**
 * SocketUtils
 * @author youfu.wang
 * @date 2015-02-15
 */
public class SocketUtils {
	public static final String ADDR_LOCALHOST="127.0.0.1";
	/**
	 * 得到当前主机地址
	 * @return
	 */
	public static String getLocalHostAddress(){
		String hostAddr=ADDR_LOCALHOST;
		try {
			Enumeration<NetworkInterface> networkInterfaces=NetworkInterface.getNetworkInterfaces();
			while(networkInterfaces.hasMoreElements()){
				NetworkInterface networkInterface=networkInterfaces.nextElement();
				if(networkInterface.isUp()){
					Enumeration<InetAddress> inetAddresses=networkInterface.getInetAddresses();
					while(inetAddresses.hasMoreElements()){
						InetAddress inetAddress=inetAddresses.nextElement();
						if(inetAddress instanceof Inet4Address){
							hostAddr=inetAddress.getHostAddress();
							if(!hostAddr.equals(ADDR_LOCALHOST)){
								break;
							}
						}
					}					
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}		
		return hostAddr;
	}
	public static void main(String[] args){
		System.out.println(SocketUtils.getLocalHostAddress());
	}
}
