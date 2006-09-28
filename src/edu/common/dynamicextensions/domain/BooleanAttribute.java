package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 */
public class BooleanAttribute extends PrimitiveAttribute {

	private Boolean defaultValue;

	public BooleanAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @return Returns the defaultValue.
     */
    public Boolean getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}