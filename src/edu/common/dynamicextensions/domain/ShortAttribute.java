
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ShortAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_SHORT_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class ShortAttribute extends Attribute implements ShortAttributeInterface
{
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -842600137818204271L;

	/**
	 * Default value for this attribute.
	 */
	protected Short defaultValue;

	/**
	 * Measurement units of the Attribute.
	 */
	protected String measurementUnits;

	/**
	 * Length of number in digits
	 */
	protected String digits;

	/**
	 * The places after the decimal point.
	 */
	protected String decimalPlaces = "0";

	/**
	 * Empty Constructor.
	 */
	public ShortAttribute()
	{
	}

	//	protected String size;
	//
	//	public String getSize()
	//	{
	//		return this.size;
	//	}
	//
	//	public void setSize(String size)
	//	{
	//		this.size = size;
	//	}

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
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

	/**
	 * This method returns the default value of this Attribute.
	 * @hibernate.property name="defaultValue" type="short" column="DEFAULT_VALUE"
	 * @return the default value of this Attribute.
	 */
	public Short getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * This method sets the default value of ShortAttribute to given Short value.
	 * @param defaultValue the value to be set as default.
	 */
	public void setDefaultValue(Short defaultValue)
	{
		this.defaultValue = defaultValue;
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
