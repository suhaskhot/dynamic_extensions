package edu.common.dynamicextensions.query.cachestore;

import java.io.InputStream;
import java.util.UUID;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

public class EhCacheManager {
	private static EhCacheManager instance;
	
	private static final String QUERY_EHCACHE_CONFIG = "query-ehcache.xml";
	
	private CacheManager manager;
	
	private EhCacheManager() {
		InputStream cfg = EhCacheManager.class.getResourceAsStream(QUERY_EHCACHE_CONFIG);
		manager = new CacheManager(cfg);					
	}
	
	public static synchronized EhCacheManager getInstance() {
		if (instance == null) {
			instance = new EhCacheManager();
		}
		
		return instance;
	}
	
	public Ehcache newCache() {
		String cacheId = UUID.randomUUID().toString();
		Cache cache = new Cache(cacheId, //cache name
				10000, //maximum elements in cache
				true,  //overflow to disk ?
				false, //eternal ?
				600,   //time to live in seconds 
				100,   //time to idle
				false, //disk persistant ?
				0);    //disk thread expiry in seconds
		
		manager.addCache(cache);
		return manager.getCache(cacheId);		
	}
	
	public void removeCache(Ehcache cache) {
		manager.removeCache(cache.getName());
	}
}
