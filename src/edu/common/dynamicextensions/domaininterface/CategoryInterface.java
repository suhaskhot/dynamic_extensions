package edu.common.dynamicextensions.domaininterface;


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

}
