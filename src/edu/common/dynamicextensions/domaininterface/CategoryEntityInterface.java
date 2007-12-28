package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.PathInterface;

public interface CategoryEntityInterface extends AbstractMetadataInterface
{
	/**
	 *
	 * @return
	 */
    Collection<CategoryAttribute> getCategoryAttributeCollection();
    /**
     *
     * @param categoryAttributeCollection
     */
    void setCategoryAttributeCollection(Collection<CategoryAttribute> categoryAttributeCollection);
    /**
     *
     * @return
     */
    Collection<CategoryEntity> getChildCategories();
    /**
     *
     * @param childCategories
     */
    void setChildCategories(Collection<CategoryEntity> childCategories);
    /**
     *
     * @return
     */
    Entity getEntity();
    /**
     *
     * @param entity
     */
    void setEntity(Entity entity);
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
}
