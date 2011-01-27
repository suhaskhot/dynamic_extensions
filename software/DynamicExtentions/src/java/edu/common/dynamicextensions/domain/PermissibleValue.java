
package edu.common.dynamicextensions.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.domaininterface.SkipLogicAttributeInterface;

/**
 * The Class PermissibleValue.
 *
 * @author sujay_narkar
 * @hibernate.class table="DYEXTN_PERMISSIBLE_VALUE"
 */
public abstract class PermissibleValue extends DynamicExtensionBaseDomainObject // NOPMD
		implements PermissibleValueInterface, Comparator<PermissibleValue>
{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1941346367802489075L;

	/** The description. */
	private String description;

	/** The numeric code. */
	private Long numericCode;


	/**
	 * Gets the numeric code.
	 * @return the numericCode
	 */
	public Long getNumericCode()
	{
		return numericCode;
	}

	/**
	 * Sets the numeric code.
	 * @param numericCode the numericCode to set
	 */
	public void setNumericCode(Long numericCode)
	{
		this.numericCode = numericCode;
	}

	/** Semantic property collection. */
	private Collection<SemanticPropertyInterface> semanticPropertyCollection =
        new HashSet<SemanticPropertyInterface>();

	/** Collection of category attributes. */
	private Collection<SkipLogicAttributeInterface> dependentSkipLogicAttributes =
	    new HashSet<SkipLogicAttributeInterface>();

	/**
	 * Gets the description.
	 *
	 * @return Returns the buttonCss.
	 *
	 * @hibernate.property name="description" type="string" column="DESCRIPTION"
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * setter method for description.
	 *
	 * @param description description of the value
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Gets the id.
	 *
	 * @return Returns the id.
	 *
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="DYEXTN_PERMISSIBLEVAL_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Gets the semantic property collection.
	 * @return Returns the semanticPropertyCollection.
	 * @hibernate.set name="semanticPropertyCollection" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_VALUE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SemanticProperty"
	 */
	public Collection getSemanticPropertyCollection()
	{
		return semanticPropertyCollection;
	}

	/**
	 * Sets the semantic property collection.
	 *
	 * @param semanticPropertyCollection The semanticPropertyCollection to set.
	 */
	public void setSemanticPropertyCollection(Collection semanticPropertyCollection)
	{
		this.semanticPropertyCollection = semanticPropertyCollection;
	}

	/**
	 * This method adds a SemanticProperty to the AbstractMetadata.
	 *
	 * @param semanticPropertyInterface A SemanticProperty to be added.
	 */
	public void addSemanticProperty(SemanticPropertyInterface semanticPropertyInterface)
	{
		if (semanticPropertyCollection == null)
		{
			semanticPropertyCollection = new HashSet<SemanticPropertyInterface>();
		}
		semanticPropertyCollection.add(semanticPropertyInterface);
	}

	/**
	 * This method removes a SemanticProperty from the AbstractMetadata.
	 *
	 * @param semanticPropertyInterface SemanticProperty to be removed.
	 */
	public void removeSemanticProperty(SemanticPropertyInterface semanticPropertyInterface)
	{
		if ((semanticPropertyCollection != null)
				&& (semanticPropertyCollection.contains(semanticPropertyInterface)))
		{
			semanticPropertyCollection.remove(semanticPropertyInterface);
		}
	}

	/**
	 * This method removes all SemanticProperties from AbstractMetadata.
	 */
	public void removeAllSemanticProperties()
	{
		if (semanticPropertyCollection != null)
		{
			semanticPropertyCollection.clear();
		}
	}

    /**
     * Gets the ordered semantic property collection.
     *
     * @return the ordered semantic property collection
     *
     * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getOrderedSemanticPropertyCollection()
     */
	public List<SemanticPropertyInterface> getOrderedSemanticPropertyCollection()
	{
		List<SemanticPropertyInterface> semanticPropertyList = new ArrayList<SemanticPropertyInterface>();

		if (semanticPropertyCollection != null && !semanticPropertyCollection.isEmpty())
		{
			semanticPropertyList.addAll(semanticPropertyCollection);
			Collections.sort(semanticPropertyList);
		}
		return semanticPropertyList;
	}

	/**
	 * Gets the dependent skip logic attributes.
	 *
	 * @return Returns the dependentSkipLogicAttributes.
	 *
	 * @hibernate.set name="dependentSkipLogicAttributes" table="DYEXTN_SKIP_LOGIC_ATTRIBUTE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PERM_VALUE_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SkipLogicAttribute"
	 */
	public Collection<SkipLogicAttributeInterface> getDependentSkipLogicAttributes()
	{
		return dependentSkipLogicAttributes;
	}

	/**
	 * Sets the dependent skip logic attributes.
	 *
	 * @param dependentSkipLogicAttributes the dependent skip logic attributes
	 */
	public void setDependentSkipLogicAttributes(
			Collection<SkipLogicAttributeInterface> dependentSkipLogicAttributes)
	{
		this.dependentSkipLogicAttributes = dependentSkipLogicAttributes;
	}

	/**
	 * This method adds a skip logic attribute.
	 *
	 * @param skipLogicAttributeInterface the skip logic attribute interface
	 */
	public void addDependentSkipLogicAttribute(
			SkipLogicAttributeInterface skipLogicAttributeInterface)
	{
		if (dependentSkipLogicAttributes == null)
		{
			dependentSkipLogicAttributes = new HashSet<SkipLogicAttributeInterface>();
		}
		dependentSkipLogicAttributes.add(skipLogicAttributeInterface);
	}

	/**
	 * This method removes a SkipLogic Attribute.
	 *
	 * @param skipLogicAttributeInterface the skip logic attribute interface
	 */
	public void removeDependentSkipLogicAttribute(
			SkipLogicAttributeInterface skipLogicAttributeInterface)
	{
		if ((dependentSkipLogicAttributes != null)
				&& (dependentSkipLogicAttributes.contains(skipLogicAttributeInterface)))
		{
			dependentSkipLogicAttributes.remove(skipLogicAttributeInterface);
		}
	}

	/**
	 * This method removes all SkipLogic Attributes.
	 */
	public void removeAllDependentSkipLogicAttributes()
	{
		if (dependentSkipLogicAttributes != null)
		{
			dependentSkipLogicAttributes.clear();
		}
	}

	/**
	 * Sets the value as string.
	 * @param value the new value as string
	 * @throws ParseException the parse exception
	 */
	public abstract void setObjectValue(Object value) throws ParseException;

}
