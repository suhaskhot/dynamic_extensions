package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

/**
 * This is a primitive attribute of type date.
 * @author geetika_bangard
 */
public interface DateAttributeInterface extends PrimitiveAttributeInterface 
{

    /**
     * The default date value. This value will be shown when user inserts a new record. 
     * @return Returns the defaultValue.
     */
    Date getDefaultValue();
    
    /**
     * @param defaultValue The defaultValue to set.
     */
    void setDefaultValue(Date defaultValue);
    
    /**
     * Date format for the date.The user input date will be validated against this format.
     * @return Returns the format.
     */
    String getFormat();
    /**
     * @param format The format to set.
     */
    void setFormat(String format);
	
}
