package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_STRING_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringAttribute extends PrimitiveAttribute {

    /**
     * Default value of this string attribute.
     */
	protected String defaultValue;
	/**
	 * The size of the field.
	 */
	protected Integer size;
	/**
	 * Empty Constructor.
	 */
	public StringAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @hibernate.property name="defaultValue" type="string" column="DEFAULT_VALUE" 
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
     * @hibernate.property name="size" type="integer" column="SIZE" 
     * @return Returns the size.
     */
    public Integer getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(Integer size) {
        this.size = size;
    }
}