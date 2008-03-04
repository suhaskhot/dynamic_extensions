
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerConstantsInterface;

/**
 * @hibernate.joined-subclass table="DYEXTN_LONG_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class LongAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			LongTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public LongAttributeTypeInformation()
	{

	}

	/** 
	 * @see edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface#getDataType()
	 */
	public String getDataType()
	{
		
		return EntityManagerConstantsInterface.LONG_ATTRIBUTE_TYPE;
	}
	
	/**
	 * 
	 */
	public PermissibleValueInterface getPermissibleValueForString(String value) {
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		LongValueInterface longValueInterface = domainObjectFactory.createLongValue();
		longValueInterface.setValue(new Long(value));
		return longValueInterface;
	}
}
