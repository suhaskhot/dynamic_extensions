package edu.common.dynamicextensions.domain;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_DATE_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class NumericAttribute123 extends PrimitiveAttribute {

	protected Long defaultValue;
	/**
	 * Mesurement units to display for this attribute
	 */
	protected String measurementUnit;
	/**
	 * The scale (number of decimal places) of an attribute if the data
	 * type is number. 
	 */
	protected Integer scale;
	/**
	 * Size of the numeric field i.e total number of digits before and after decimal places.
	 */
	protected Integer size;
	/**
	 *Empty Constructor.
	 */
	public NumericAttribute123(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

    /**
     * @hibernate.property name="defaultValue" type="long" column="DEFAULT_VALUE" 
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
     * @hibernate.property name="measurementUnit" type="string" column="MEASUREMENT_UNIT" 
     * @return Returns the measurementUnit.
     */
    public String getMeasurementUnit() {
        return measurementUnit;
    }
    /**
     * @param measurementUnit The measurementUnit to set.
     */
    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
    /**
     *  @hibernate.property name="scale" type="integer" column="SCALE" 
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
     *  @hibernate.property name="size" type="integer" column="SIZE" 
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