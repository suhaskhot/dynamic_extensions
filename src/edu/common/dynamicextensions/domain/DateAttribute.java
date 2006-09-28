package edu.common.dynamicextensions.domain;

import java.util.Date;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 */
public class DateAttribute extends PrimitiveAttribute {

	protected Date defaultValue;
	/**
	 * format of the attribute value (Data entry/display)
	 */
	protected String format;

	public DateAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @return Returns the defaultValue.
     */
    public Date getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }
    /**
     * @return Returns the format.
     */
    public String getFormat() {
        return format;
    }
    /**
     * @param format The format to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }
}