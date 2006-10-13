package edu.common.dynamicextensions.domaininterface;


/**
 * This is a primitive attribute of type double.Using this information a coulmn of type double
 *  is prepared in the dynamically created tables.
 * @author geetika_bangard
 */
public interface DoubleAttributeInterface extends AttributeInterface
{
  
    /**
     * Default value of type double.
     * @return Returns the defaultValue.
     */
    Double getDefaultValue();
    
    /**
     * @param defaultValue The defaultValue to set.
     */
    void setDefaultValue(Double defaultValue);
    
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
  
  }
