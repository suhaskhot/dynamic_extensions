package edu.common.dynamicextensions.domaininterface;


/**
 * This object stores the permissible value of boolean type.This is a user defined value.
 * @author geetika_bangard
 */
public interface BooleanValueInterface extends PermissibleValueInterface 
{
           
   	/**
   	 * @return Returns the value.
   	 */
   	Boolean getValue();
   	/**
   	 * @param value The value to set.
   	 */
   	void setValue(Boolean value);
}
