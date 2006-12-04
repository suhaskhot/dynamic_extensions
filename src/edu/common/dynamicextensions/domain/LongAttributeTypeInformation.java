
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface;

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
	 * This method returns the measurement units of this Attribute.
	 * @hibernate.property name="measurementUnits" type="string" column="MEASUREMENT_UNITS"  
	 * @return the measurement units of this Attribute.
	 */
	public String getMeasurementUnits()

	{
		return this.measurementUnits;
	}

}
