
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE" 
 *
 */
public abstract class PermissibleValue extends DynamicExtensionBaseDomainObject
		implements PermissibleValueInterface
{

	/**
	 * 
	 */
	protected String description;

	/**
	 * Semantic property collection.
	 */

	protected Collection<SemanticPropertyInterface> semanticPropertyCollection = new HashSet<SemanticPropertyInterface>();

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
