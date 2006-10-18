
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class IntegerValue extends PermissibleValue implements IntegerValueInterface{
    
    /**
     * 
     */
    protected Integer value;

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		
		
	}

	/**
     * 
     * @hibernate.property name="value" type="integer" column="VALUE" 
	 * @return Returns the value.
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Integer value) {
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
