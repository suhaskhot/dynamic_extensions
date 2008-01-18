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

}