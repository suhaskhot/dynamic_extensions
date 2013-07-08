package edu.common.dynamicextensions.domain.nui;

import java.util.HashMap;
import java.util.Map;

public class ValidationRule {	
	private String name;
	
	private Map<String, String> params = new HashMap<String, String>(); 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}	
}
