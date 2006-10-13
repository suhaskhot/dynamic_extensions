package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type short. 
 * @author geetika_bangard
 */
public interface ShortValueInterface extends PermissibleValueInterface 
{
	/**
	 * @return Returns the value.
	 */
	 Short getValue();
	/**
	 * @param value The value to set.
	 */
	void setValue(Short value);
}
