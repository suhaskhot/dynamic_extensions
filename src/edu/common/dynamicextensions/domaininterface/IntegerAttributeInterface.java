package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface IntegerAttributeInterface extends AttributeInterface {

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
	
}
