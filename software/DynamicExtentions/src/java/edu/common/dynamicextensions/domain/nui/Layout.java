package edu.common.dynamicextensions.domain.nui;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;

public  class Layout extends DynamicExtensionBaseDomainObject implements Serializable {

	private static final long serialVersionUID = -2688596455566764947L;
	
	private String requiredFieldIndicatior;
	
	private String requiredFieldWarningMessage;

	@Override
	public Long getId() {
		return id;
	}

	public String getRequiredFieldIndicatior() {
		return requiredFieldIndicatior;
	}

	public void setRequiredFieldIndicatior(String requiredFieldIndicatior) {
		this.requiredFieldIndicatior = requiredFieldIndicatior;
	}

	public String getRequiredFieldWarningMessage() {
		return requiredFieldWarningMessage;
	}

	public void setRequiredFieldWarningMessage(String requiredFieldWarningMessage) {
		this.requiredFieldWarningMessage = requiredFieldWarningMessage;
	}
}