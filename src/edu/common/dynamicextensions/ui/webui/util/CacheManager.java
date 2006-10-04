
package edu.common.dynamicextensions.ui.webui.util;

import java.util.HashMap;
import java.util.Map;




/**
 * @author deepti_shelar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CacheManager {
	private static CacheManager cacheManager = null;
	private CacheManager() {

	}
	private static Map cacheMap = new HashMap();


	public static CacheManager getInstance() {
		if(cacheManager == null ){
			return new CacheManager();
		}
		return cacheManager;
	}
	public void addObjectToCache(String sessionId, FormDetailsObject formDetailsObject) {
		cacheMap.put(sessionId, formDetailsObject);
	}
	public FormDetailsObject getObjectFromCache(String sessionId) {
		FormDetailsObject result = null;
		result = (FormDetailsObject)cacheMap.get(sessionId);
		return result;
	}
	public void removeObjectToCache(String sessionId, FormDetailsObject formDetailsObject) {
		cacheMap.remove(sessionId);
	}
}
