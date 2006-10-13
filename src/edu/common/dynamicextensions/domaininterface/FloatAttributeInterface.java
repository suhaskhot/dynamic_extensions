package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface FloatAttributeInterface extends AttributeInterface {
    
    /**
     * @return Returns the defaultValue.
     */
    public Float getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Float defaultValue);
    /**
     * @return Returns the measurementUnits.
     */
    public String getMeasurementUnits();
    /**
     * @param measurementUnits The measurementUnits to set.
     */
    public void setMeasurementUnits(String measurementUnits);
    
}
