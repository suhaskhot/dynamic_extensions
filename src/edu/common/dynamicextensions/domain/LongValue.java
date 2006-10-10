
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_LONG_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class LongValue extends PermissibleValue implements LongValueInterface{
    
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
     * @hibernate.property name="value" type="long" column="VALUE"  
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
}
