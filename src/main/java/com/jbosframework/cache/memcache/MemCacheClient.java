package com.jbosframework.cache.memcache;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.jbosframework.cache.memcache.MemCacheResource;
import com.jbosframework.core.io.Resource;
import com.jbosframework.cache.provider.CacheClient;
/**
 * MemCache Client
 * @author youfu.wang
 * @version 1.0
 */
public class MemCacheClient implements CacheClient{
	private MemCachedClient client=new MemCachedClient();  
		
	public void init(Resource resource){
		MemCacheResource memCacheResource=new MemCacheResource();
		memCacheResource.readResource(resource);
		SockIOPool pool = SockIOPool.getInstance(); 
		pool.setServers(memCacheResource.getServers());
		pool.setInitConn(memCacheResource.getInitConn());
		pool.setMinConn(memCacheResource.getMinConn());
		pool.setMaxConn(memCacheResource.getMaxConn());
		pool.setMaxIdle(memCacheResource.getMaxIdle());
		pool.setMaintSleep(memCacheResource.getMaintSleep());
		pool.setNagle(false);  
        pool.setSocketTO(30);  
        pool.setSocketConnectTO(0);  
        pool.initialize();    
	}
	public void setData(String key,Object value){
		client.set(key, value);
	}
	public Object getData(String key){
		return client.get(key);
	}
	public void deleteData(String key){
		client.delete(key);
	}
}
