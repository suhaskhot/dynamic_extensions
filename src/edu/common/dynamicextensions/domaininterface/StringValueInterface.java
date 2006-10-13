package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type String. 
 * @author geetika_bangard
 */
public interface StringValueInterface extends PermissibleValueInterface 
{
    
	/**
	 * @return Returns the value.
	 */
	String getValue();
	/**
	 * @param value The value to set.
	 */
	void setValue(String value);
}
