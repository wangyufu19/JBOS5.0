package com.jbosframework.cache.provider;

/**
 * Cache Provider
 * @author youfu.wang
 * @version 1.0
 */
public interface CacheProvider {
	
	public String getConfigLocation();

	public void setConfigLocation(String configLocation);
	
	public CacheClient getCacheClient();
}
