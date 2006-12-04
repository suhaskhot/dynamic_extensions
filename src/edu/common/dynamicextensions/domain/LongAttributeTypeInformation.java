
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_LONG_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class LongAttributeTypeInformation extends AttributeTypeInformation implements LongTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 3294237564427180163L;

	/**
	 * Measurement units of this Attribute.
	 */
	protected String measurementUnits;

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
		return measurementUnits;
	}

	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits the measurement units to be set.
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
	protected String decimalPlaces = "0";

	/**
	 * @see edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface#getDecimalPlaces()
	 */
	public String getDecimalPlaces()
	{
		return this.decimalPlaces;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface#setDecimalPlaces(java.lang.String)
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
