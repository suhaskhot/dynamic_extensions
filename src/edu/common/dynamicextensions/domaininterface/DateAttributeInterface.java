package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

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

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException ;
}
