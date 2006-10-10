package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface LongAttributeInterface extends PrimitiveAttributeInterface {
  
    /**
     * @return Returns the defaultValue.
     */
    public Long getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Long defaultValue);
    /**
     * @return Returns the measurementUnits.
     */
    public String getMeasurementUnits();
    /**
     * @param measurementUnits The measurementUnits to set.
     */
    public void setMeasurementUnits(String measurementUnits);
}
