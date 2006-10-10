package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface StringValueInterface extends PermissibleValueInterface {
    
	/**
	 * @return Returns the value.
	 */
	public String getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value);
}
