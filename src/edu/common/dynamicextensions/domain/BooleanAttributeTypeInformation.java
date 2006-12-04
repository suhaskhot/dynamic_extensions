package edu.common.dynamicextensions.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class BooleanAttributeTypeInformation extends AttributeTypeInformation
{
    
	/**
	 * Empty Constructor.
	 */
	public BooleanAttributeTypeInformation()
	{

	}
	

}