//package com.jbosframework.cache.memcache;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import com.jbosframework.core.io.ClassPathResource;
//import com.jbosframework.core.io.Resource;
//import com.jbosframework.cache.memcache.MemCacheClient;
//import com.jbosframework.cache.provider.CacheClient;
//
///**
// * MemCache Client Factory
// * @author youfu.wang
// * @version 1.0
// */
//public class MemCacheClientFactory {
//	public static final String configuration="memcached.xml";
//	private static CacheClient cacheClient=null;
//	private static Log log=LogFactory.getLog(MemCacheClientFactory.class);
//	/**
//	 * Build memcache client
//	 * @param resource
//	 * @return
//	 */
//	public static CacheClient buildMemCacheClient(Resource resource){
//
//		if(cacheClient==null){
//			synchronized (CacheClient.class) {
//				if(resource==null)
//					resource=new ClassPathResource(configuration);
//				cacheClient=new MemCacheClient();
//				cacheClient.init(resource);
//			}
//		}
//		return cacheClient;
//	}
//}
