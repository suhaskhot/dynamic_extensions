
package edu.common.dynamicextensions.util;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * <p>Title: DynamicExtensionsCacheManager Class</p>
 * <p>Description:Singleton factory class handling caching operations.</p>
 */

public class DynamicExtensionsCacheManager
{

	/**
	 * Singleton reference to the DynamicExtensionsCacheManager
	 */
	private static DynamicExtensionsCacheManager deCacheManager;

	/**
	 *  reference to CacheManager
	 */
	private static CacheManager manager;

	/**
	 *  reference to Cache
	 */
	private static Cache cache;

	/**
	 * Protected constructor of the singleton class
	 * @throws CacheException
	 */
	protected DynamicExtensionsCacheManager() throws CacheException
	{
		// create singleton instance of CacheManager
		CacheManager.create();
		// get instance of cachemanager
		manager = CacheManager.getInstance();
		// get cache for DynamicExtension which is configured in ehcache.xml
		cache = manager.getCache("cacheForDE");
		
	}

	/**
	 * Method returning the singleton instance of the
	 * CatissueCoreCacheManager
	 *
	 * @return CatissueCoreCacheManager
	 * @throws CacheException
	 */
	public static synchronized DynamicExtensionsCacheManager getInstance() throws CacheException
	{
		if (deCacheManager == null)
		{
			deCacheManager = new DynamicExtensionsCacheManager();
		}
		return deCacheManager;
	}

	/**
	 * Method used to add Serializable object to cache
	 * @param key - Serializable key
	 * @param value - Serializable value
	 */
	public void addObjectToCache(Serializable key, Serializable value)
	{
		// creating elemtnt from key,value
		Element element = new Element(key, value);
		// Adding element to cache
		if(cache!=null)
		cache.put(element);
	}
	
	/**
	 * Method used to remove Serializable object to cache
	 * @param key - Serializable key
	 */
	public void removeObjectFromCache(Serializable key)
	{
		// remove element to cache
		if(cache!=null)
		cache.remove(key);
	}


	/**
	 * Method used to get Serializable object from cache
	 * @param key - Serializable Key
	 * @return - Serializable Object
	 * @throws CacheException
	 * @throws IllegalStateException
	 */
	public Serializable getObjectFromCache(Serializable key) throws IllegalStateException, CacheException
	{
		Element element = null;
		if(cache!=null)
		element = cache.get(key);
		if (element != null)
		{
		   return element.getValue();
		}		
	    return null;
	}

	/**
	 * @throws CacheException
	 */
	public void shutdown() throws CacheException
	{
		// shutting down the instance of cacheManager
		CacheManager.getInstance().shutdown();

	}

}