
package edu.common.dynamicextensions.ui.webui.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author deepti_shelar
 * Class that handles the complete logic of storing and retrieving objects from the cache/session.
 */
public class CacheManager
{
	/**
	 * Create a new CacheManager instance
	 *
	 */
	public CacheManager()
	{

	}

	static Map<String, Object> cacheMap;

	/**
	 * Add object to cache 
	 * @param request : HTTP Request object
	 * @param key : Key to be used while storing object in cache
	 * @param formDetailsObject : Object to be stored in cache
	 */
	@SuppressWarnings("unchecked")
	public static void addObjectToCache(HttpServletRequest request, String key, Object formDetailsObject)
	{
		HttpSession session = request.getSession();
		cacheMap = (Map) session.getAttribute(Constants.CACHE_MAP);

		if (cacheMap == null)
		{
			cacheMap = new HashMap<String, Object>();
			session.setAttribute(Constants.CACHE_MAP, cacheMap);
		}
		cacheMap.put(key, formDetailsObject);

	}

	@SuppressWarnings("unchecked")
	public static Object getObjectFromCache(HttpServletRequest request, String key)
	{
		HttpSession session = request.getSession();
		Object result = null;

		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map<String, Object>) session.getAttribute(Constants.CACHE_MAP);
			result = cacheMap.get(key);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static void removeObjectFromCache(HttpServletRequest request, String key)
	{
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map<String, Object>) session.getAttribute(Constants.CACHE_MAP);
			cacheMap.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	public static void clearCache(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map<String, Object>) session.getAttribute(Constants.CACHE_MAP);
			cacheMap.clear();
		}
	}
}
