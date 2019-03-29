package com.jbosframework.cache.provider;
import com.jbosframework.core.io.Resource;

/**
 * Cache Client Interface
 * @author youfu.wang
 * @version 1.0
 */
public interface CacheClient {
	
	public void init(Resource resource);
	
	public void setData(String key,Object value);
	
	public Object getData(String key);
	
	public void deleteData(String key);
}
