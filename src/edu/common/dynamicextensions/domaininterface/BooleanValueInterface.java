package edu.common.dynamicextensions.domaininterface;


/**
 * @author geetika_bangard
 */
public interface BooleanValueInterface extends PermissibleValueInterface {
           
   	/**
   	 * @return Returns the value.
   	 */
   	public Boolean getValue();
   	/**
   	 * @param value The value to set.
   	 */
   	public void setValue(Boolean value);
}
