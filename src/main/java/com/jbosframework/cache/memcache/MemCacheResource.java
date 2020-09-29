//package com.jbosframework.cache.memcache;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.jdom.Element;
//import com.jbosframework.core.io.Resource;
//import com.jbosframework.core.jdom.DocumentFactory;
//import com.jbosframework.core.jdom.JDOMDocumentFactory;
//import com.jbosframework.common.utils.NumberUtils;
//import com.jbosframework.common.utils.StringUtils;
//
///**
// * MemCache Resource
// * @author youfu.wang
// * @version 1.0
// */
//public class MemCacheResource {
//	//MemCache连接池可用的服务器
//	private static final String MEMCACHE_SERVERS="servers";
//	private static final String MEMCACHE_SERVER="server";
//	//初始连接数
//	private static final String MEMCACHE_INITCONN="initConn";
//	//最少可用连接数
//	private static final String MEMCACHE_MINCONN="minConn";
//	//最大可用连接数
//	private static final String MEMCACHE_MAXCONN="maxConn";
//	//连接池的最长等待时间(毫秒)
//	private static final String MEMCACHE_MAXIDLE="maxIdle";
//	//连接池维护线程的睡眠时间
//	private static final String MEMCACHE_MAINTSLEEP="maintSleep";
//	//是否压缩数据
//	private static final String MEMCACHE_COMPRESS_ENABLE="compressEnable";
//	//压缩的数据的阈值
//	private static final String MEMCACHE_COMPRESS_THRESHOLD="compressThreshold";
//
//	public static final String[] DEFAULT_SERVER={"127.0.0.1:11211"};
//	private String[] servers=null;
//	private int initConn=5;
//	private int minConn=5;
//	private int maxConn=50;
//	private int maxIdle=60000;
//	private int maintSleep=30;
//	private boolean compressEnable=true;
//	private int compressThreshold=1024;
//
//
//	/**
//	 * Read memCache resource
//	 * @param resource
//	 */
//	public void readResource(Resource resource){
//		DocumentFactory documentFactory=JDOMDocumentFactory.getInstance();
//		Object obj=null;
//		try {
//			obj=documentFactory.getDocument(resource.getInputStream());
//			readResource(obj);
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//	}
//	/**
//	 * Read memCache resource
//	 * @param obj
//	 */
//	private void readResource(Object obj) {
//		if (obj == null)
//			return;
//		if (obj instanceof org.jdom.Document) {
//			org.jdom.Document doc = (org.jdom.Document) obj;
//			org.jdom.Element rootE=doc.getRootElement();
//			if(rootE==null)
//				return;
//			Element serversE=rootE.getChild(MemCacheResource.MEMCACHE_SERVERS);
//			if(serversE==null){
//				servers=DEFAULT_SERVER;
//			}else{
//				List list=serversE.getChildren(MemCacheResource.MEMCACHE_SERVER);
//				List<String> serversList=new ArrayList<String>();
//				if(list!=null){
//					for(int i=0;i<list.size();i++){
//						Element serverE=(Element)list.get(i);
//						serversList.add(serverE.getTextTrim());
//					}
//					servers=new String[serversList.size()];
//					serversList.toArray(servers);
//				}
//			}
//			Element initConnE=rootE.getChild(MemCacheResource.MEMCACHE_INITCONN);
//			if(initConnE!=null){
//				if(!"".equals(StringUtils.replaceNull(initConnE.getTextTrim()))&&NumberUtils.isNumeric(StringUtils.replaceNull(initConnE.getTextTrim()))){
//					initConn=Integer.parseInt(StringUtils.replaceNull(initConnE.getTextTrim()));
//				}
//			}
//			Element minConnE=rootE.getChild(MemCacheResource.MEMCACHE_MINCONN);
//			if(minConnE!=null){
//				if(!"".equals(StringUtils.replaceNull(minConnE.getTextTrim()))&&NumberUtils.isNumeric(StringUtils.replaceNull(minConnE.getTextTrim()))){
//					minConn=Integer.parseInt(StringUtils.replaceNull(minConnE.getTextTrim()));
//				}
//			}
//			Element maxConnE=rootE.getChild(MemCacheResource.MEMCACHE_MAXCONN);
//			if(maxConnE!=null){
//				if(!"".equals(StringUtils.replaceNull(maxConnE.getTextTrim()))&&NumberUtils.isNumeric(StringUtils.replaceNull(maxConnE.getTextTrim()))){
//					maxConn=Integer.parseInt(StringUtils.replaceNull(maxConnE.getTextTrim()));
//				}
//			}
//			Element maxIdleE=rootE.getChild(MemCacheResource.MEMCACHE_MAXIDLE);
//			if(maxIdleE!=null){
//				if(!"".equals(StringUtils.replaceNull(maxIdleE.getTextTrim()))&&NumberUtils.isNumeric(StringUtils.replaceNull(maxIdleE.getTextTrim()))){
//					maxIdle=Integer.parseInt(StringUtils.replaceNull(maxIdleE.getTextTrim()));
//				}
//			}
//			Element maintSleepE=rootE.getChild(MemCacheResource.MEMCACHE_MAINTSLEEP);
//			if(maintSleepE!=null){
//				if(!"".equals(StringUtils.replaceNull(maintSleepE.getTextTrim()))&&NumberUtils.isNumeric(StringUtils.replaceNull(maintSleepE.getTextTrim()))){
//					maintSleep=Integer.parseInt(StringUtils.replaceNull(maintSleepE.getTextTrim()));
//				}
//			}
//		}
//	}
//	public String[] getServers() {
//		return servers;
//	}
//	public void setServers(String[] servers) {
//		this.servers = servers;
//	}
//	public int getInitConn() {
//		return initConn;
//	}
//	public void setInitConn(int initConn) {
//		this.initConn = initConn;
//	}
//	public int getMinConn() {
//		return minConn;
//	}
//	public void setMinConn(int minConn) {
//		this.minConn = minConn;
//	}
//	public int getMaxConn() {
//		return maxConn;
//	}
//	public void setMaxConn(int maxConn) {
//		this.maxConn = maxConn;
//	}
//	public int getMaxIdle() {
//		return maxIdle;
//	}
//	public void setMaxIdle(int maxIdle) {
//		this.maxIdle = maxIdle;
//	}
//	public int getMaintSleep() {
//		return maintSleep;
//	}
//	public void setMaintSleep(int maintSleep) {
//		this.maintSleep = maintSleep;
//	}
//	public boolean isCompressEnable() {
//		return compressEnable;
//	}
//	public void setCompressEnable(boolean compressEnable) {
//		this.compressEnable = compressEnable;
//	}
//	public int getCompressThreshold() {
//		return compressThreshold;
//	}
//	public void setCompressThreshold(int compressThreshold) {
//		this.compressThreshold = compressThreshold;
//	}
//}
//
