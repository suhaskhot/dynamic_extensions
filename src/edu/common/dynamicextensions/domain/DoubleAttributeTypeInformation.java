
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.joined-subclass table="DYEXTN_DOUBLE_TYPE_INFO" 
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 * @author sujay_narkar
 *
 */
public class DoubleAttributeTypeInformation extends AttributeTypeInformation implements DoubleTypeInformationInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -748391527722552390L;
	/**
	 * 
	 */
	protected String measurementUnits;

	/**
	 * Empty Constructor.
	 */
	public DoubleAttributeTypeInformation()
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
	 * @see edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface#getDecimalPlaces()
	 */
	public String getDecimalPlaces()
	{
		return this.decimalPlaces;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface#setDecimalPlaces(java.lang.String)
	 */
	public void setDecimalPlaces(String decimalPlaces)
	{
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface#getDigits()
	 */
	public String getDigits()
	{
		return this.digits;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface#setDigits(java.lang.String)
	 */
	public void setDigits(String digits)
	{
		this.digits = digits;
	}

	protected String size;


}
