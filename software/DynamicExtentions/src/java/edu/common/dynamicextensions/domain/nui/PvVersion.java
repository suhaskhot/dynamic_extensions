package edu.common.dynamicextensions.domain.nui;

import java.util.Date;
import java.util.List;

public class PvVersion {
	private Date activationDate;
	
	private List<PermissibleValue> permissibleValues;
	
	private PermissibleValue defaultValue;

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public List<PermissibleValue> getPermissibleValues() {
		return permissibleValues;
	}

	public void setPermissibleValues(List<PermissibleValue> permissibleValues) {
		this.permissibleValues = permissibleValues;
	}

	public PermissibleValue getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(PermissibleValue defaultValue) {
		this.defaultValue = defaultValue;
	}
}
