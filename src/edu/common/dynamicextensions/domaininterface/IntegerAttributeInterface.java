package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type integar.Using this information a column of type integer is prepared.
 * @author geetika_bangard
 */
public interface IntegerAttributeInterface extends AttributeInterface 
{

	/**
	 * Default value of type integer.
	 * @return Returns the defaultValue.
	 */
	Integer getDefaultValue();
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	void setDefaultValue(Integer defaultValue);
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
	 * 
	 * @return Number of decimal places
	 */
	public String getDecimalPlaces();
	/**
	 * 
	 * @param decimalPlaces Number of decimal places
	 */
	public void setDecimalPlaces(String decimalPlaces);

	/**
	 * 
	 * @return Number of digits
	 */
	public String getDigits();
	/**
	 * 
	 * @param digits Number of digits
	 */
	public void setDigits(String digits);
	public String getSize();
	public void setSize(String size);
	
}
