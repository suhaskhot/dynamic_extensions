
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ShortTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_SHORT_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class ShortAttributeTypeInformation extends AttributeTypeInformation implements ShortTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -842600137818204271L;
	/**
	 * 
	 */
	protected String measurementUnits;

	/**
	 * Number of digits
	 */
	protected String digits;
	/**
	 * Number of decimal places
	 */
	protected String decimalPlaces = "0";

	/**
	 * @see edu.common.dynamicextensions.domaininterface.ShortTypeInformationInterface#getDecimalPlaces()
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
		return measurementUnits;
	}

	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits The measurementUnits to set.
	 */
	public void setMeasurementUnits(String measurementUnits)
	{
		this.measurementUnits = measurementUnits;
	}
}
