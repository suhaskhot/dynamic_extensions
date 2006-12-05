
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerTypeInformationInterface;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 *
 */
public class IntegerAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			IntegerTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public IntegerAttributeTypeInformation()
	{

	}
}
