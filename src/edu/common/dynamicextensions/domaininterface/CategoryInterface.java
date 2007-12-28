package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.CategoryEntity;

public interface CategoryInterface extends AbstractMetadataInterface {
    /**
     *
     * @return
     */
    public CategoryEntity getRootCategoryElement();
    /**
     *
     * @param rootCategoryElement
     */
    public void setRootCategoryElement(CategoryEntity rootCategoryElement);

}
