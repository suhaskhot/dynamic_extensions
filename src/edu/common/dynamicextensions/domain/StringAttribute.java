
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_STRING_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class StringAttribute extends Attribute implements StringAttributeInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8457915421460880246L;

	/**
	 * Default value of this string attribute.
	 */
	protected String defaultValue;

	/**
	 * The lenght of the string.
	 */
	protected Integer size;

	/**
	 * Empty Constructor.
	 */
	public StringAttribute()
	{
	}

	/**
	 * This method returns the default value of this Attribute.
	 * @hibernate.property name="defaultValue" type="string" column="DEFAULT_VALUE" 
	 * @return the default value of this Attribute.
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * This method sets the default value of ShortAttribute to given Short value.
	 * @param defaultValue the value to be set as default.
	 */
	public void setDefaultValue(String defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	 * This method returns the length of the string.
	 * @hibernate.property name="size" type="integer" column="MAX_SIZE" 
	 * @return Returns the length of the string.
	 */
	public Integer getSize()
	{
		return size;
	}

	/**
	 * This method sets the length of the string.
	 * @param size the lenght of the string to be set.
	 */
	public void setSize(Integer size)
	{
		this.size = size;
	}

	/**
	 * Set all values from the form
	 * @param abstractActionForm the ActionForm
	 * @throws AssignDataException if data is not in proper format.
	 */
	public void setAllValues(AbstractActionForm abstractActionForm) throws AssignDataException
	{
	}

}