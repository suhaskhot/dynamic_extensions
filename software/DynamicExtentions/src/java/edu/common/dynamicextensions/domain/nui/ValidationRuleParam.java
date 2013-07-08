package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;

public class ValidationRuleParam extends DynamicExtensionBaseDomainObject {

	private static final long serialVersionUID = 8316933933329823350L;
	
	private String name;
	
	private String value;

	@Override
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
