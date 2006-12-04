
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;

/**
 * This Class represent the Floating value Attribute of the Entity.
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 */
public class FloatAttributeTypeInformation extends NumericAttributeTypeInformation implements FloatTypeInformationInterface
{
	/**
	 * Empty Constructor.
	 */
	public FloatAttributeTypeInformation()
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
