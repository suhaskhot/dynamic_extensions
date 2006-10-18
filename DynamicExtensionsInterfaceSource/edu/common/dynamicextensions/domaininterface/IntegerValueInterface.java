package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type integer. 
 * @author geetika_bangard
 */
public interface IntegerValueInterface extends PermissibleValueInterface 
{

	/**
	 * @return Returns the value.
	 */
	 Integer getValue();
	/**
	 * @param value The value to set.
	 */
	 void setValue(Integer value);
}
