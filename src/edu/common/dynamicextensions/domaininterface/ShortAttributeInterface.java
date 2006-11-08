
package edu.common.dynamicextensions.domaininterface;

/**
 * This is a primitive attribute of type short.Using this information a column of type short is prepared.
 * @author geetika_bangard
 */
public interface ShortAttributeInterface extends AttributeInterface
{

	/**
	 * This method returns the default value of this Attribute.
	 * @return the default value of this Attribute.
	 */
	Short getDefaultValue();

	/**
	 * This method sets the default value of ShortAttribute to given Short value.
	 * @param defaultValue the value to be set as default.
	 */
	void setDefaultValue(Short defaultValue);

	/**
	 * The measurement units are shown in the dynamically created user interface.
	 * The measurement units are meter,kg,cm etc.They are displayed after the user input control.
	 * This method returns the measurement units of this Attribute.
	 * @return the measurement units of this Attribute.
	 */
	String getMeasurementUnits();

	/**
	 * This method sets the measurement units of this Attribute.
	 * @param measurementUnits The measurementUnits to set.
	 */
	void setMeasurementUnits(String measurementUnits);

	/**
	 * This method returns the places after the decimal point.
	 * @return the places after the decimal point.
	 */
	String getDecimalPlaces();

	/**
	 * This method sets the places after the decimal point of the DoubleAttribue.
	 * @param decimalPlaces the places after the decimal point to be set.
	 */
	void setDecimalPlaces(String decimalPlaces);

	/**
	 * This method returns the length of the number in digits.
	 * @return the length of the number in digits. 
	 */
	String getDigits();

	/**
	 * This method sets the length of the number in digits.
	 * @param digits the length of the number in digits.
	 */
	void setDigits(String digits);

	//	public String getSize();
	//	public void setSize(String size);
}
