package edu.common.dynamicextensions.domain;

import java.util.Date;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATE_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DateAttribute extends PrimitiveAttribute {

    /**
     *  Default value of this date attribute.
     */
	protected Date defaultValue;
	/**
	 * format of the attribute value (Data entry/display)
	 */
	protected String format;
	/**
	 * Empty Constructor
	 */
	public DateAttribute(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	
    /**
     * @hibernate.property name="defaultValue" type="date" column="DEFAULT_VALUE" 
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
     * @hibernate.property name="format" type="string" column="FORMAT" 
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

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}