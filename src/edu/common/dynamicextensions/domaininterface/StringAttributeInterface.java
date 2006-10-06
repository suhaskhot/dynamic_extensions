package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface StringAttributeInterface extends PrimitiveAttributeInterface {

    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue);
    /**
     * @return Returns the size.
     */
    public Integer getSize();
    /**
     * @param size The size to set.
     */
    public void setSize(Integer size);

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException ;
}
