package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;

/**
 *
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class Category extends AbstractMetadata implements CategoryInterface
{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4234527890L;

    /**
     * rootCategoryElement.
     */
    protected CategoryEntity rootCategoryElement;

    /**
     *
     *
     */
    public Category()
    {
    	super();
    }

    /**
     * @hibernate.many-to-one column="ROOT_CATEGORY_ELEMENT" cascade="all" class="edu.common.dynamicextensions.domain.CategoryEntity"
     * @return the rootCategoryElement.
     */
    public CategoryEntityInterface getRootCategoryElement() {
        return (CategoryEntityInterface)rootCategoryElement;
    }

    /**
     * @param rootCategoryElement the rootCategoryElement to set
     */
    public void setRootCategoryElement(CategoryEntityInterface rootCategoryElement) {
        this.rootCategoryElement = (CategoryEntity)rootCategoryElement;
    }
    
    /**
     * @param categoryEntityName
     * @return
     */
    public CategoryEntityInterface getCategoryEntityByName(String categoryEntityName)
    {
    	return getCategoryEntity(this.getRootCategoryElement(),
        		categoryEntityName);
    }
    
    /**
     * @param categoryEntity
     * @param categoryEntityName
     * @return
     */
    private CategoryEntityInterface getCategoryEntity(CategoryEntityInterface categoryEntity,
    		String categoryEntityName)
    {
    	CategoryEntityInterface searchedCategoryEntity = null;
    	if(categoryEntity == null)
    	{
    		return searchedCategoryEntity;
    	}
    	if(categoryEntity.getName().equals(categoryEntityName))
    	{
    		return categoryEntity;
    	}
    	for(CategoryEntityInterface categoryEntityInterface: categoryEntity.getChildCategories())
    	{
    		if(categoryEntityInterface.getName().equals(categoryEntityName))
    		{
    			searchedCategoryEntity =  categoryEntityInterface;
    			break;
    		}
    		
    		if(categoryEntityInterface.getChildCategories().size() > 0)
    		{
    			searchedCategoryEntity =  getCategoryEntity(categoryEntityInterface, categoryEntityName);
    		}
    		
    	}
    	
    	return searchedCategoryEntity;
    }

}