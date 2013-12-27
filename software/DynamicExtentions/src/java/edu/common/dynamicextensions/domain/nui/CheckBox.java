
package edu.common.dynamicextensions.domain.nui;

import java.util.Collections;
import java.util.List;

public class CheckBox extends Control {
	private boolean defaultValueChecked;

	public boolean isDefaultValueChecked() {
		return defaultValueChecked;
	}

	public void setDefaultValueChecked(boolean defaultValueChecked) {
		this.defaultValueChecked = defaultValueChecked;
	}

	// TODO: Be database agnostic
	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), "DECIMAL(19, 6)"));
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean fromString(String value) {
		return (value != null) && (value.equals("1") || value.equalsIgnoreCase("true"));
	}
	
	@Override
	public String toString(Object value) {
		return (value == null || value.toString().equals("false") || value.toString().equals("0")) ? "0" : "1";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (defaultValueChecked ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		CheckBox other = (CheckBox) obj;
		if (defaultValueChecked != other.defaultValueChecked) {
			return false;
		}
		
		return true;
	}	
}
