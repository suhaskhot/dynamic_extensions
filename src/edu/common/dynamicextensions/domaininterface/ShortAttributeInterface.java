package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface ShortAttributeInterface extends PrimitiveAttributeInterface {
    
    /**
     * @return Returns the defaultValue.
     */
    public Short getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Short defaultValue);
    /**
     * @return Returns the measurementUnits.
     */
    public String getMeasurementUnits();
    /**
     * @param measurementUnits The measurementUnits to set.
     */
    public void setMeasurementUnits(String measurementUnits);
   
   
}
