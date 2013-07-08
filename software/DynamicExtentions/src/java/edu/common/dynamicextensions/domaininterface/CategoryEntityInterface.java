
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

public interface CategoryEntityInterface extends AbstractEntityInterface
{

	/**
	 *
	 * @return
	 */
	Collection<CategoryAttributeInterface> getCategoryAttributeCollection();

	/**
	 *
	 * @param categoryAttributeCollection
	 */
	void setCategoryAttributeCollection(
			Collection<CategoryAttributeInterface> categoryAttributeCollection);

	/**
	 *
	 * @return
	 */
	Collection<CategoryEntityInterface> getChildCategories();

	/**
	 *
	 * @param childCategories
	 */
	void setChildCategories(Collection<CategoryEntityInterface> childCategories);

	/**
	 *
	 * @param categoryEntityInterface
	 */
	void addChildCategory(CategoryEntityInterface categoryEntityInterface);

	/**
	 *
	 * @return
	 */
	EntityInterface getEntity();

	/**
	 *
	 * @param entity
	 */
	void setEntity(EntityInterface entity);

	/**
	 * This method adds an AbstractAttribute to the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute AbstractAttribute to be added.
	 */
	void addCategoryAttribute(CategoryAttributeInterface categoryAttributeInterface);

	/**
	 * This method removes an AbstractAttribute from the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute an AbstractAttribute to be removed.
	 */
	void removeCategoryAttribute(CategoryAttributeInterface categoryAttributeInterface);

	/**
	 * This method removes all the AssociationInterface from thePath's Collection of Association.
	 * @param associationInterface an AssociationInterface to be removed.
	 */
	void removeAllCategoryAttributes();

	/**
	 * This method adds an AbstractAttribute to the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute AbstractAttribute to be added.
	 */
	void addPath(PathInterface pathInterface);

	/**
	 * This method removes an AbstractAttribute from the Entity's Collection of AbstractAttribute.
	 * @param abstractAttribute an AbstractAttribute to be removed.
	 */
	void removePath(PathInterface pathInterface);

	/**
	 * This method removes all the AssociationInterface from thePath's Collection of Association.
	 * @param associationInterface an AssociationInterface to be removed.
	 */
	void removeAllPaths();

	/**
	 *
	 * @return
	 */
	CategoryInterface getCategory();

	/**
	 *
	 * @param categoryInterface
	 */
	void setCategory(CategoryInterface categoryInterface);

	/**
	 * Return the path of this category entity from root entity.
	 * @return path.
	 */
	PathInterface getPath();

	/**
	 * sets the path from root entity.
	 * @param path path of the category entity from root.
	 */
	void setPath(PathInterface path);

	/**
	 * return whether this category entity will have only one record or many records.
	 * @return number of entries allowed.
	 */
	Integer getNumberOfEntries();

	/**
	 * sets the number of entries for this category entity.
	 * @param numberOfEntries number of entries allowed.
	 */
	void setNumberOfEntries(Integer numberOfEntries);

	/**
	 * returns the category association collection in this category entity.
	 * @return categroy association collection.
	 */
	Collection<CategoryAssociationInterface> getCategoryAssociationCollection();

	/**
	 * sets the category assocation collection.
	 * @param categoryAssociationCollection association collection.
	 */
	void setCategoryAssociationCollection(
			Collection<CategoryAssociationInterface> categoryAssociationCollection);

	/**
	 * returns the parent category entity of this category entity.
	 * @return parent category entity.
	 */
	CategoryEntityInterface getParentCategoryEntity();

	/**
	 * sets the parent category entity.
	 * @param parentCategoryEntity parent category entity.
	 */
	void setParentCategoryEntity(CategoryEntityInterface parentCategoryEntity);

	/**
	 * It will return all the category attributes present in this category entity.
	 * @return collection of category attributes.
	 */
	Collection<CategoryAttributeInterface> getAllCategoryAttributes();

	/**
	 * Searches the association with the given name.
	 * @param associationName name of the association to be searched.
	 * @return category association with gven name, else null.
	 */
	CategoryAssociationInterface getAssociationByName(String associationName);

	/**
	 * It will search the category attribute with given name.
	 * @param attributeName name of the attribute which is needed.
	 * @return attribute with given name, else null.
	 */
	CategoryAttributeInterface getAttributeByName(String attributeName);

	/**
	 * It will search the category attribute with given entity attribute name.
	 * @param entityAttributeName name of the entity attribute.
	 * @return category attribute with given name, else null.
	 */
	CategoryAttributeInterface getAttributeByEntityAttributeName(String entityAttributeName);

	/**
	 * returns the tree parent category Entity.
	 * @return tree parent category Entity.
	 */
	CategoryEntityInterface getTreeParentCategoryEntity();

	/**
	 * Sets the Tree ParentCategory Entity.
	 * @param treeParentCategoryEntity tree parent category entity to be set.
	 */
	void setTreeParentCategoryEntity(CategoryEntityInterface treeParentCategoryEntity);

	/**
	 * Denotes is table is created for this category entity or not.
	 * @return is table created or not.
	 */
	Boolean isCreateTable();

	

}
