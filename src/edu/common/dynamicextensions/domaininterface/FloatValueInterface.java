package edu.common.dynamicextensions.domaininterface;


/**
 * The permissible value of type float. 
 * @author geetika_bangard
 */
public interface FloatValueInterface extends PermissibleValueInterface 
{
    
 	/**
 	 * @return Returns the value.
 	 */
 	 Float getValue();
 	/**
 	 * @param value The value to set.
 	 */
 	void setValue(Float value);
}
