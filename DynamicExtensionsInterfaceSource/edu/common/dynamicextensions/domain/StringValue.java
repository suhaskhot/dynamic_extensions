
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_STRING_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringValue extends PermissibleValue implements StringValueInterface{
    
    protected String value;
    
    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}

	/**
     * @hibernate.property name="value" type="string" column="VALUE"  
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#getValueAsObject()
	 */
	public Object getValueAsObject()
	{
		return value;
	}
}
