
package edu.common.dynamicextensions.ui.webui.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.util.global.Constants;


/**
 * @author deepti_shelar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CacheManager {
	public CacheManager() {

	}	
	static Map cacheMap;
	public static void addObjectToCache(HttpServletRequest request,String key, Object formDetailsObject) {
		HttpSession session = request.getSession();
		if(session.getAttribute(Constants.CACHE_MAP) == null) {
			cacheMap = new HashMap();
		} else {
			cacheMap = (Map)session.getAttribute(Constants.CACHE_MAP);
			cacheMap.put(key, formDetailsObject);	
		}		
	}
	public static Object getObjectFromCache(HttpServletRequest request , String key) {
		HttpSession session = request.getSession();
		Object result = null;
		if(session.getAttribute(Constants.CACHE_MAP) != null) {
			cacheMap = (Map)session.getAttribute(Constants.CACHE_MAP);
			result = cacheMap.get(key);
		} 
		return result;
	}
	public static void removeObjectFromCache(HttpServletRequest request , String key) {
		HttpSession session = request.getSession();
		if(session.getAttribute(Constants.CACHE_MAP) != null) {
			cacheMap = (Map)session.getAttribute(Constants.CACHE_MAP);
			cacheMap.remove(key);
		} 
	}
	public static void clearCache(HttpServletRequest request ) {
		HttpSession session = request.getSession();
		if(session.getAttribute(Constants.CACHE_MAP) != null) {
			cacheMap = (Map)session.getAttribute(Constants.CACHE_MAP);
			cacheMap.clear();
		}
	}
}
