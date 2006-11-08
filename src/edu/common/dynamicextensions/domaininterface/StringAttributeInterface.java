
package edu.common.dynamicextensions.domaininterface;

/**
 * This is a primitive attribute of type String.Using this information a column of type string is prepared.
 * @author geetika_bangard
 */
public interface StringAttributeInterface extends AttributeInterface
{

	/**
	 * This method returns the default value of this Attribute.
	 * @return the default value of this Attribute.
	 */
	String getDefaultValue();

	/**
	 * This method sets the default value of ShortAttribute to given Short value.
	 * @param defaultValue the value to be set as default.
	 */
	void setDefaultValue(String defaultValue);

	/**
	 * This method returns the length of the string.
	 * @return Returns the length of the string.
	 */
	Integer getSize();

	/**
	 * This method sets the lenght of the string.
	 * @param size the lenght of the string to be set.
	 */
	void setSize(Integer size);

}
