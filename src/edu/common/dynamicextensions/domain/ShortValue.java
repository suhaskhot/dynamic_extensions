
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_SHORT_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class ShortValue extends PermissibleValue implements ShortValueInterface{
	
	/**
	 * 
	 */
	protected Short value;
	/**
	 * 
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * @hibernate.property name="value" type="short" column="VALUE"  
	 * @return Returns the value.
	 */
	public Short getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Short value) {
		this.value = value;
	}
}
