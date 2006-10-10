package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface ShortValueInterface extends PermissibleValueInterface {
  
	/**
	 * @return Returns the value.
	 */
	public Short getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(Short value);
}
