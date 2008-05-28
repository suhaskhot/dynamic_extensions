package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;


public interface CategoryInterface extends AbstractMetadataInterface {
    /**
     *
     * @return
     */
    public CategoryEntityInterface getRootCategoryElement();
    /**
     *
     * @param rootCategoryElement
     */
    public void setRootCategoryElement(CategoryEntityInterface rootCategoryElement);
    
    /**
     * @param categoryEntityName
     * @return
     */
    public CategoryEntityInterface getCategoryEntityByName(String categoryEntityName);
    
    /**
     * @return
     */
    public Collection<CategoryEntityInterface> getRelatedAttributeCategoryEntityCollection();
    
    /**
     * @param relatedAttributeCategoryEntityCollection
     */
    public void setRelatedAttributeCategoryEntityCollection(Collection<CategoryEntityInterface> relatedAttributeCategoryEntityCollection);
    
    /**
     * @param categoryEntity
     */
    public void addRelatedAttributeCategoryEntity(CategoryEntityInterface categoryEntity);
}
