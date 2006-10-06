package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

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
    
    /**
     * 
     */
    public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
}
