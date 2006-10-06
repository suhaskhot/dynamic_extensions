package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface BooleanAttributeInterface extends PrimitiveAttributeInterface {

    /**
     * @return Returns the defaultValue.
     */
    public Boolean getDefaultValue();
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Boolean defaultValue);

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
}
