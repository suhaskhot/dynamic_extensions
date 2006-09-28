package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class StringAttribute extends PrimitiveAttribute {

	protected String defaultValue;
	/**
	 * Gets or sets the size of a field.
	 */
	protected int size;

	public StringAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    /**
     * @return Returns the size.
     */
    public int getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(int size) {
        this.size = size;
    }
}