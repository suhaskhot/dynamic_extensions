package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

/**
 * @author geetika_bangard
 */
public interface DateAttributeInterface extends PrimitiveAttributeInterface {

    /**
     * @return Returns the defaultValue.
     */
    public Date getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Date defaultValue);
    /**
     * @return Returns the format.
     */
    public String getFormat();
    /**
     * @param format The format to set.
     */
    public void setFormat(String format);
	
}
