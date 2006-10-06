
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 *
 */
public class IntegerAttribute extends PrimitiveAttribute implements IntegerAttributeInterface{
	/**
	 * Default value for this attribute.
	 */
	protected Integer defaultValue;
    /**
     * 
     */
    protected String measurementUnits;
    
    
	
	/**
	 * Empty Constructor.
	 */
	public IntegerAttribute (){
		
	}
	
	/**
	 * 
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
	}
	/**
     * @hibernate.property name="defaultValue" type="integer" column="DEFAULT_VALUE"
	 * @return Returns the defaultValue.
	 */
	public Integer getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
     * @hibernate.property name="measurementUnits" type="string" column="MEASUREMENT_UNITS"  
	 * @return Returns the measurementUnits.
	 */
	public String getMeasurementUnits() {
		return measurementUnits;
	}
	/**
	 * @param measurementUnits The measurementUnits to set.
	 */
	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}
}
