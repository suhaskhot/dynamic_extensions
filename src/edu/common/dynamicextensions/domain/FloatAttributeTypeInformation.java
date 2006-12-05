
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;

/**
 * This Class represent the Floating value Attribute of the Entity.
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 */
public class FloatAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			FloatTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public FloatAttributeTypeInformation()
	{

	}
}
