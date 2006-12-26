
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.BooleanTypeInformationInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class BooleanAttributeTypeInformation extends AttributeTypeInformation
		implements
			BooleanTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public BooleanAttributeTypeInformation()
	{

	}

}