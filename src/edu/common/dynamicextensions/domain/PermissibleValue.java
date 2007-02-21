
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE" 
 *
 */
public abstract class PermissibleValue extends SemanticAnnotatable
		implements
			PermissibleValueInterface
{

	/**
	 * 
	 */
	protected String description;

	/**
	 * @hibernate.property name="description" type="string" column="DESCRIPTION" 
	 * @return Returns the buttonCss.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * setter method for description
	 * @param description description of the value
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @see edu.common.dynamicextensions.domaininterface.PermissibleValueInterface#getValueAsObject()
	 */
	public abstract Object getValueAsObject();

	/**
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_PERMISSIBLEVAL_SEQ" 
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}
}
