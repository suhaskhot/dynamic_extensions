package edu.common.dynamicextensions.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class BooleanAttribute extends PrimitiveAttribute {
    /**
     * Default value for this attribute.
     */
	private Boolean defaultValue;

	/**
	 * Empty Constructor.
	 */
	public BooleanAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @hibernate.property name="defaultValue" type="boolean" column="DEFAULT_VALUE" 
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

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}