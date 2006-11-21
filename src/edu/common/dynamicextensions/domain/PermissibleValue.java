
package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE" 
 *
 */
public abstract class PermissibleValue extends AbstractDomainObject
		implements
			java.io.Serializable,
			PermissibleValueInterface
{

	/**
	 * Unique identifier for the object
	 */
	protected Long id;

	/**
	 * 
	 */
	protected String description;

	/**
	 * Semantic property collection.
	 */

	protected Collection semanticPropertyCollection;

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
	 * @return id
	 */
	public Long getSystemIdentifier()
	{
		return id;
	}


	/**
	 * @param id long
	 */
	public void setSystemIdentifier(Long id)
	{
		this.id = id;

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

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @hibernate.set name="semanticPropertyCollection" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_VALUE_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SemanticProperty"
	 * @return Returns the semanticPropertyCollection.
	 */
	public Collection getSemanticPropertyCollection()
	{
		return semanticPropertyCollection;
	}

	/**
	 * @param semanticPropertyCollection The semanticPropertyCollection to set.
	 */
	public void setSemanticPropertyCollection(Collection semanticPropertyCollection)
	{
		this.semanticPropertyCollection = semanticPropertyCollection;
	}
}
