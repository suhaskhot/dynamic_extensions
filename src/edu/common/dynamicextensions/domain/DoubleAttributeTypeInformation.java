
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface;

/**
 * @hibernate.joined-subclass table="DYEXTN_DOUBLE_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class DoubleAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			DoubleTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public DoubleAttributeTypeInformation()
	{

	}
}
