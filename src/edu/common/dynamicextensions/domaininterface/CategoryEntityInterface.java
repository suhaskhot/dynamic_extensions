package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.Entity;

public interface CategoryEntityInterface extends AbstractMetadataInterface
{
	/**
	 *
	 * @return
	 */
    public Collection<CategoryAttribute> getCategoryAttributeCollection();
    /**
     *
     * @param categoryAttributeCollection
     */
    public void setCategoryAttributeCollection(Collection<CategoryAttribute> categoryAttributeCollection);
    /**
     *
     * @return
     */
    public Collection<CategoryEntity> getChildCategories();
    /**
     *
     * @param childCategories
     */
    public void setChildCategories(Collection<CategoryEntity> childCategories);
    /**
     *
     * @return
     */
    public Entity getEntity();
    /**
     *
     * @param entity
     */
    public void setEntity(Entity entity);
}
