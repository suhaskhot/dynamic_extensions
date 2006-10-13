package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type long. 
 * @author geetika_bangard
 */
public interface LongValueInterface extends PermissibleValueInterface 
{
	/**
	 * @return Returns the value.
	 */
	Long getValue();
	/**
	 * @param value The value to set.
	 */
	void setValue(Long value);
}
