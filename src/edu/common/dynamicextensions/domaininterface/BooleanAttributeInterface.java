
package edu.common.dynamicextensions.domaininterface;

/**
 * This is a boolean type of primitive attribute.
 * @author geetika_bangard
 */
public interface BooleanAttributeInterface extends AttributeInterface
{

	/**
	 * This method returns the default value of this Attribute. 
	 * @return the default value of this Attribute.
	 */
	Boolean getDefaultValue();

	/**
	 * This method sets the default value of this Attribute.
	 * @param defaultValue the defaultValue to be set.
	 */
	void setDefaultValue(Boolean defaultValue);

}
