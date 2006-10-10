package edu.common.dynamicextensions.domaininterface;

import java.sql.Date;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author geetika_bangard
 */
public interface DateValueInterface extends PermissibleValueInterface {

    public void setAllValues(AbstractActionForm arg0) throws AssignDataException;
    
  	/**
	 * @return Returns the value.
	 */
	public Date getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(Date value) ;
}
