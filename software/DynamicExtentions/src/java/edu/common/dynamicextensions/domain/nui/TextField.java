/**
 * 
 */
package edu.common.dynamicextensions.domain.nui;

public abstract class TextField extends Control {

	private static final long serialVersionUID = -6827748743355556283L;

	private int noOfColumns;
	
	private String defaultValue="";
	
	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}		
}
