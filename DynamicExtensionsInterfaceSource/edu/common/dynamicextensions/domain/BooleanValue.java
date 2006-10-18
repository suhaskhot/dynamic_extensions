
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_CONCEPT_VALUE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class BooleanValue extends PermissibleValue implements BooleanValueInterface
{
    
    protected Boolean value;
    
    /**
     * 
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException 
    {
     // TODO Auto-generated method stub
    }

   
	/**
     * @hibernate.property name="value" type="boolean" column="VALUE" 
	 * @return Returns the value.
	 */
	public Boolean getValue() 
	{
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Boolean value)
	{
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
