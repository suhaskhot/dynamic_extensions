
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

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

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{

		return EntityManagerConstantsInterface.FLOAT_ATTR_TYPE;
	}

	/**
	 * 
	 */
	public PermissibleValueInterface getPermissibleValueForString(String value)
	{
		DomainObjectFactory factory = DomainObjectFactory.getInstance();
		FloatValueInterface floatValue = factory.createFloatValue();
		floatValue.setValue(new Float(value));
		
		return floatValue;
	}
	
}
