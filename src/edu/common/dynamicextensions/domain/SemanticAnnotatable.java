
package edu.common.dynamicextensions.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;

/**
 * @author Rahul Ner
 */
abstract public class SemanticAnnotatable extends DynamicExtensionBaseDomainObject
		implements
			SemanticAnnotatableInterface
{

	/**
	 * Semantic property collection.
	 */
	protected Collection<SemanticPropertyInterface> semanticPropertyCollection = new HashSet<SemanticPropertyInterface>();

	/**
	 * This method returns the Collection of SemanticProperties of the AbstractMetadata.
	 * @hibernate.set name="semanticPropertyCollection" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ABSTRACT_METADATA_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.SemanticProperty"
	 * @hibernate.cache  usage="read-write"
	 * @return the Collection of SemanticProperties of the AbstractMetadata.
	 */
	public Collection<SemanticPropertyInterface> getSemanticPropertyCollection()
	{
		return semanticPropertyCollection;
	}

	/**
	 * This method sets the semanticPropertyCollection to the given Collection of SemanticProperties.
	 * @param semanticPropertyCollection the Collection of SemanticProperties to be set.
	 */
	public void setSemanticPropertyCollection(
			Collection<SemanticPropertyInterface> semanticPropertyCollection)
	{
		this.semanticPropertyCollection = semanticPropertyCollection;
	}

	/**
	 * This method adds a SemanticProperty to the AbstractMetadata.
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
	 * @param semanticPropertyInterface A SemanticProperty to be removed.
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
	 * @see edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface#getOrderedSemanticPropertyCollection()
	 */
	public List getOrderedSemanticPropertyCollection()
	{
		List<SemanticPropertyInterface> semanticPropertyList = new ArrayList<SemanticPropertyInterface>();

		if (this.semanticPropertyCollection != null && !this.semanticPropertyCollection.isEmpty())
		{
			semanticPropertyList.addAll(this.semanticPropertyCollection);
			Collections.sort(semanticPropertyList);
		}
		return semanticPropertyList;
	}
}
