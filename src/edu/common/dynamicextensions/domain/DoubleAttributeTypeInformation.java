
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

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
		// TODO Auto-generated constructor stub
	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{

		return EntityManagerConstantsInterface.DOUBLE_ATTRIBUTE_TYPE;
	}

	/**
	 * 
	 */
	public PermissibleValueInterface getPermissibleValueForString(String value)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		DoubleValueInterface doubleValue = factory.createDoubleValue();
		doubleValue.setValue(new Double(value));

		return doubleValue;
	}

}
