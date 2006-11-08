
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:06 PM
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class BooleanAttribute extends Attribute implements BooleanAttributeInterface
{
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8224962984309772858L;

	/**
	 * Default value for this attribute.
	 */
	private Boolean defaultValue;

	/**
	 * Empty Constructor.
	 */
	public BooleanAttribute()
	{
	}

	/**
	 * This method returns the default value of this Attribute. 
	 * @hibernate.property name="defaultValue" type="boolean" column="DEFAULT_VALUE" 
	 * @return the default value of this Attribute.
	 */
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * This method sets the default value of this Attribute.
	 * @param defaultValue the defaultValue to be set.
	 */
	public void setDefaultValue(Boolean defaultValue)
	{
		this.defaultValue = defaultValue;
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