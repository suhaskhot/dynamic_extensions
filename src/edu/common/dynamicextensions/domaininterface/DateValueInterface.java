package edu.common.dynamicextensions.domaininterface;

import java.sql.Date;

/**
 * @author geetika_bangard
 */
public interface DateValueInterface extends PermissibleValueInterface {
  	/**
	 * @return Returns the value.
	 */
	public Date getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(Date value) ;
}
