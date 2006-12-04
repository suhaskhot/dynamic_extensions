
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * This Class represent the Floating value Attribute of the Entity.
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 */
public class FloatAttributeTypeInformation extends AttributeTypeInformation implements FloatTypeInformationInterface
{

	/**
	 * 
	 */
	protected String measurementUnits;

	/**
	 * Empty Constructor.
	 */
	public FloatAttributeTypeInformation()
	{

	}

	/**
	 * @hibernate.property name="measurementUnits" type="string" column="MEASUREMENT_UNITS"  
	 * @return Returns the measurementUnits.
	 */
	public String getMeasurementUnits()
	{
		return measurementUnits;
	}

	/**
	 * @param measurementUnits The measurementUnits to set.
	 */
	public void setMeasurementUnits(String measurementUnits)
	{
		this.measurementUnits = measurementUnits;
	}

	/**
	 * Number of digits
	 */
	protected String digits;
	/**
	 * Number of decimal places
	 */
	protected String decimalPlaces;

	/**
	 * This method returns the places after the decimal point.
	 * @return the places after the decimal point.
	 */
	public String getDecimalPlaces()
	{
		return this.decimalPlaces;
	}

	/**
	 * This method sets the places after the decimal point of the DoubleAttribue.
	 * @param decimalPlaces the places after the decimal point to be set.
	 */
	public void setDecimalPlaces(String decimalPlaces)
	{
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * This method returns the length of the number in digits.
	 * @return the length of the number in digits. 
	 */
	public String getDigits()
	{
		return this.digits;
	}

	/**
	 * This method sets the length of the number in digits.
	 * @param digits the length of the number in digits.
	 */
	public void setDigits(String digits)
	{
		this.digits = digits;
	}

}
