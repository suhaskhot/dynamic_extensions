package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_STRING_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringAttribute extends PrimitiveAttribute implements StringAttributeInterface{

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
     * @hibernate.property name="size" type="integer" column="MAX_SIZE" 
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

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
	}
}