
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 *
 */
public class IntegerAttributeTypeInformation extends AttributeTypeInformation implements IntegerTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 3243707308687701983L;

	/**
	 * Default value for this attribute.
	 */
	protected Integer defaultValue;
	/**
	 * Measurement units of the Attribute.
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

	/**
	 * Empty Constructor.
	 */
	public IntegerAttributeTypeInformation()
	{

	}

	/**
	 * This method returns the measurement units of this Attribute.
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

}
