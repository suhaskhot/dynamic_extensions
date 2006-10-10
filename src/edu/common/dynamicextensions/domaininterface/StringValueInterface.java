package edu.common.dynamicextensions.domaininterface;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface StringValueInterface extends PermissibleValueInterface {

    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException;

	/**
	 * @return Returns the value.
	 */
	public String getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value);
}
