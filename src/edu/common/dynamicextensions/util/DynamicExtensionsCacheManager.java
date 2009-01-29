
package edu.common.dynamicextensions.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: DynamicExtensionsCacheManager Class</p>
 * <p>Description:Singleton factory class handling caching operations.</p>
 */

public class DynamicExtensionsCacheManager
{

	/**
	 * 
	 */
	private static DynamicExtensionsCacheManager deCacheManager;
	/**
	 * 
	 */
	private static Map containercacheMap;

	/**
	 * 
	 */
	protected DynamicExtensionsCacheManager()
	{
		if (containercacheMap == null)
		{
			containercacheMap = new HashMap();
		}

	}

	/**
	 * @return
	 */
	public static synchronized DynamicExtensionsCacheManager getInstance()
	{
		if (deCacheManager == null)
		{
			deCacheManager = new DynamicExtensionsCacheManager();
		}
		return deCacheManager;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addObjectToCache(Object key, Object value)
	{

		if (containercacheMap != null)
		{
			containercacheMap.put(key, value);
		}
	}

	/**
	 * @param key
	 */
	public void removeObjectFromCache(Object key)
	{

		if (containercacheMap != null)
		{
			containercacheMap.remove(key);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getObjectFromCache(Object key)
	{

		if (containercacheMap != null)
		{
			return containercacheMap.get(key);
		}

		return null;
	}

}