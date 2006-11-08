
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE" 
 */
public abstract class PermissibleValue extends AbstractDomainObject implements java.io.Serializable, PermissibleValueInterface
{

	/**
	 * Unique identifier for the object
	 */
	protected Long id;

	/**
	 * This method returns the Unique identifier of this Object.
	 * @return the Unique identifier of this Object.
	 */
	public Long getSystemIdentifier()
	{
		return id;
	}

	/**
	 * This method sets the Unique identifier of this Object.
	 * @param id the Unique identifier to be set.
	 */
	public void setSystemIdentifier(Long id)
	{
		this.id = id;
	}

	/**
	 * This method returns the value of PermissibleValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public abstract Object getValueAsObject();

	/**
	 * This method returns the Unique identifier of this Object.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_PERMISSIBLEVAL_SEQ" 
	 * @return the Unique identifier of this Object.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * This method sets the Unique identifier of this Object.
	 * @param id the Unique identifier to be set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
}
