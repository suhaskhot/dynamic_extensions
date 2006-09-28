package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class NumericAttribute extends PrimitiveAttribute {

	protected Long defaultValue;
	/**
	 * Mesurement units to display for this attribute
	 */
	protected String measurementUnits;
	/**
	 * Gets or sets the scale (number of decimal places) of an attribute if the data
	 * type is number. 
	 */
	protected Integer scale;
	protected Integer size;

	public NumericAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	
	

    /**
     * @return Returns the defaultValue.
     */
    public Long getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Long defaultValue) {
        this.defaultValue = defaultValue;
    }
    /**
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
    /**
     * @return Returns the scale.
     */
    public Integer getScale() {
        return scale;
    }
    /**
     * @param scale The scale to set.
     */
    public void setScale(Integer scale) {
        this.scale = scale;
    }
    /**
     * @return Returns the size.
     */
    public Integer getSize() {
        return size;
    }
    /**
     * @param size The size to set.
     */
    public void setSize(Integer size) {
        this.size = size;
    }
}