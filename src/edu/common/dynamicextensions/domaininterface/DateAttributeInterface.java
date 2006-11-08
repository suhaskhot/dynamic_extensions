
package edu.common.dynamicextensions.domaininterface;

import java.util.Date;

/**
 * This is a primitive attribute of type date.
 * @author geetika_bangard
 */
public interface DateAttributeInterface extends AttributeInterface
{

	/**
	 * This method returns the default value of the DateAttribute.
	 * @return the default value of the DateAttribute.
	 */
	Date getDefaultValue();

	/**
	 * This method sets the default value of the DateAttribute.
	 * @param defaultValue The default value to be set.
	 */
	void setDefaultValue(Date defaultValue);

	/**
	 * This method returns the format of the DateAttribute.
	 * @return the format of the DateAttribute.
	 */
	String getFormat();

	/**
	 * This method sets the format of the DateAttribute.
	 * @param format the format to be set.
	 */
	void setFormat(String format);

}
