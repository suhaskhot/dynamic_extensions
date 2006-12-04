
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ShortTypeInformationInterface;

/**
 * @hibernate.joined-subclass table="DYEXTN_SHORT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class ShortAttributeTypeInformation extends NumericAttributeTypeInformation
		implements
			ShortTypeInformationInterface
{

	/**
	 * Empty Constructor.
	 */
	public ShortAttributeTypeInformation()
	{

	}
	
	/**
	 * This method returns the measurement units of this Attribute.
	 * @hibernate.property name="measurementUnits" type="string" column="MEASUREMENT_UNITS"  
	 * @return the measurement units of this Attribute.
	 */
	public String getMeasurementUnits()

	{
		return this.measurementUnits;
	}
}
