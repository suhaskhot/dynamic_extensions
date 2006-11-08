
package edu.common.dynamicextensions.domain;

import java.util.Date;

import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATE_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DateAttribute extends Attribute implements DateAttributeInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 5655678242696814276L;

	/**
	 *  Default value of this date attribute.
	 */
	protected Date defaultValue;

	/**
	 * format of the attribute value (Data entry/display)
	 */
	protected String format;

	/**
	 * Empty Constructor
	 */
	public DateAttribute()
	{
	}

	/**
	 * This method returns the default value of the DateAttribute.
	 * @hibernate.property name="defaultValue" type="date" column="DEFAULT_VALUE" 
	 * @return the default value of the DateAttribute.
	 */
	public Date getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * This method sets the default value of the DateAttribute.
	 * @param defaultValue The default value to be set.
	 */
	public void setDefaultValue(Date defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	 * This method returns the format of the DateAttribute.
	 * @hibernate.property name="format" type="string" column="FORMAT" 
	 * @return the format of the DateAttribute.
	 */
	public String getFormat()
	{
		return format;
	}

	/**
	 * This method sets the format of the DateAttribute.
	 * @param format the format to be set.
	 */
	public void setFormat(String format)
	{
		this.format = format;
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