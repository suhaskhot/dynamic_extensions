
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.NumericTypeInformationInterface;

/**
 * @author Rahul Ner
 * @hibernate.joined-subclass 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public abstract class NumericAttributeTypeInformation extends AttributeTypeInformation implements NumericTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	protected static final long serialVersionUID = -842600137818204271L;

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
	 * Empty Constructor.
	 */
	protected NumericAttributeTypeInformation()
	{

	}


	/**
	 * @see edu.common.dynamicextensions.domaininterface.NumericTypeInformationInterface#getMeasurementUnits()
	 */
	public abstract String getMeasurementUnits();
	

	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits The measurementUnits to set.
	 */
	public void setMeasurementUnits(String measurementUnits)
	{
		this.measurementUnits = measurementUnits;
	}

	/**
	 * 
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
