
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;

/**
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ASSOCIATION"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class CategoryAssociation extends BaseAbstractAttribute implements CategoryAssociationInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 12345678L;
	
	/**
	 * 
	 */
	protected Collection<CategoryEntityInterface> categoryEntityCollection = new HashSet<CategoryEntityInterface>();

	/**
	 * @hibernate.set name="categoryEntityCollection" table="DYEXTN_CATEGORY_ENTITY"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ASSOCIATION_ID"
	 * @hibernate.cache usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CategoryEntity"
	 * @return the categoryEntityCollection
	 */
	private Collection<CategoryEntityInterface> getCategoryEntityCollection()
	{
		return categoryEntityCollection;
	}

	/**
	 * @param categoryEntityCollection the categoryEntityCollection to set
	 */
	private void setCategoryEntityCollection(Collection<CategoryEntityInterface> categoryEntityCollection)
	{
		this.categoryEntityCollection = categoryEntityCollection;
	}
	
	public CategoryEntityInterface getCategoryEntity()
	{
		CategoryEntityInterface categoryEntity = null;
		if (categoryEntityCollection != null && !categoryEntityCollection.isEmpty())
		{
			Iterator categoryEntityCollectionIterator = categoryEntityCollection.iterator();
			categoryEntity = (CategoryEntity) categoryEntityCollectionIterator.next();
		}
		return categoryEntity;
	}

	/**
	 *
	 */
	public void setCategoryEntity(CategoryEntityInterface categoryEntity)
	{
		if (categoryEntityCollection == null)
		{
			categoryEntityCollection = new HashSet<CategoryEntityInterface>();
		}
		else
		{
			categoryEntityCollection.clear();
		}
		this.categoryEntityCollection.add(categoryEntity);
	}

}
