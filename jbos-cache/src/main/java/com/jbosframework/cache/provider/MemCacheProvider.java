//package com.jbosframework.cache.provider;
//import com.jbosframework.cache.memcache.MemCacheClientFactory;
//import com.jbosframework.core.io.ClassPathResource;
//
///**
// * MemCache Provider
// * @author youfu.wang
// * @version 1.0
// */
//public class MemCacheProvider implements CacheProvider{
//	private String configLocation;
//
//	public String getConfigLocation() {
//		return configLocation;
//	}
//
//	public void setConfigLocation(String configLocation) {
//		this.configLocation = configLocation;
//	}
//
//	public CacheClient getCacheClient(){
//		CacheClient cacheClient=MemCacheClientFactory.buildMemCacheClient(new ClassPathResource(configLocation));
//		return cacheClient;
//	}
//
//}
