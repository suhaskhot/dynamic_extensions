
package edu.common.dynamicextensions.domaininterface;

/**
 * This is a primitive attribute of type float.Using this information a column of type float is prepared.
 * @author geetika_bangard
 */
public interface FloatTypeInformationInterface extends AttributeTypeInformationInterface
{

	/**
	 * The measurement units are shown in the dynamically created user interface.
	 * The measurement units are meter,kg,cm etc.They are displayed after the user input control.
	 * @return Returns the measurementUnits.
	 */
	String getMeasurementUnits();

	/**
	 * @param measurementUnits The measurementUnits to set.
	 */
	void setMeasurementUnits(String measurementUnits);

	/**
	 * @return Number of decimal places
	 */
	String getDecimalPlaces();

	/**
	 * 
	 * @param decimalPlaces Number of decimal places
	 */
	void setDecimalPlaces(String decimalPlaces);

	/**
	 * 
	 * @return Number of digits
	 */
	String getDigits();

	/**
	 * 
	 * @param digits Number of digits
	 */
	void setDigits(String digits);
	// 	public String getSize();
	//	public void setSize(String size);
}
