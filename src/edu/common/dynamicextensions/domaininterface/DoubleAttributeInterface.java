package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type double.Using this information a coulmn of type double
 *  is prepared in the dynamically created tables.
 * @author geetika_bangard
 */
public interface DoubleAttributeInterface extends AttributeInterface
{
  
    /**
     * This method returns the default value of this Attribute.
     * @return the default value of this Attribute.
     */
    Double getDefaultValue();
    
    /**
     * This method sets the default value of DoubleAttribute to given Double value.
     * @param defaultValue the value to be set as default.
     */
    void setDefaultValue(Double defaultValue);
    
    /**
	 * This method returns the measurement units of this Attribute.
	 * The measurement units are shown in the dynamically created user interface.
     * The measurement units are meter,kg,cm etc.
	 * @return the measurement units of this Attribute.
	 */
    String getMeasurementUnits();
    
    /**
     * This method sets the measurement units of this Attribute.
     * @param measurementUnits the measurement units to be set.
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
//
//	public void setSize(String size);
	
  }
