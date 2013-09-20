package edu.common.dynamicextensions.nutility;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;

public class ContainerCache {
	private static final int MAX_ELEMENTS = 50;
	
	private static final ContainerCache instance = new ContainerCache();
	
	private Map<Long, Container> cacheMap = Collections.synchronizedMap(
			new LinkedHashMap<Long, Container>(MAX_ELEMENTS + 2, 0.75f, true) {
				private static final long serialVersionUID = -9164975002022907616L;
				
				@Override
				public boolean removeEldestEntry(Map.Entry<Long, Container> eldest) {
					return size() > MAX_ELEMENTS;
				}
				
			});
	
	private ContainerCache() {
		
	}
	
	public static ContainerCache getInstance() {
		return instance;
	}
	
	public Container get(Long id) {
		return cacheMap.get(id);
	}
	
	public Container put(Long id, Container container) {
		if (id == null || container == null) {
			return null;
		}
		
		return cacheMap.put(id, container);
	}

	public void clear() {
		cacheMap.clear();
		
	}

	public boolean isEmpty() {
		return cacheMap.isEmpty();
	}

	public Container remove(Long id) {
		return cacheMap.remove(id);
	}

	public int size() {
		return cacheMap.size();
	}

	public Collection<Container> values() {
		return cacheMap.values();
	}
}
