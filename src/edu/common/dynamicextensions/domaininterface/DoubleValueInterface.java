package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type double. 
 * @author geetika_bangard
 */
public interface DoubleValueInterface extends PermissibleValueInterface 
{

	/**
     * Double permissible value.
	 * @return Returns the value.
	 */
	 Double getValue();
	/**
	 * @param value The value to set.
	 */
	void setValue(Double value);
}
