package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface LongValueInterface extends PermissibleValueInterface {
       
	/**
	 * @return Returns the value.
	 */
	public Long getValue();
	/**
	 * @param value The value to set.
	 */
	public void setValue(Long value);
}
