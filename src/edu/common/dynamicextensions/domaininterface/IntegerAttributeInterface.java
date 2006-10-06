package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface IntegerAttributeInterface extends PrimitiveAttributeInterface {

	/**
	 * @return Returns the defaultValue.
	 */
	public Integer getDefaultValue();
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(Integer defaultValue);
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
