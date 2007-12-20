package edu.common.dynamicextensions.domain;

import java.util.HashSet;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;

/**
 * 
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ENTITY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 *
 */
public class CategoryEntity extends AbstractEntity implements CategoryEntityInterface {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6534523890L;
    
    protected int noOfEntries;
    
    protected Set<CategoryEntity> childCategories = new HashSet<CategoryEntity>();

    protected Set<CategoryAttribute> categoryAttributeCollection = new HashSet<CategoryAttribute>();

    protected Entity entity;
    
    protected Path pathFromParent;
    
    public CategoryEntity()
    {
        
    }

    /**
     * hibernate.property
     * @return the noOfEntries
     */
    public int getNoOfEntries() {
        return noOfEntries;
    }

    /**
     * @param noOfEntries the noOfEntries to set
     */
    public void setNoOfEntries(int noOfEntries) {
        this.noOfEntries = noOfEntries;
    }

    
    /**
     * @hibernate.set cascade="all"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CategoryAttribute"
     * @hibernate.collection-key column="ATTRIBUTE_CATEGORY_ENTITY_ID"  
     * @return the categoryAttributeCollection
     */

    public Set<CategoryAttribute> getCategoryAttributeCollection() 
    {
        return categoryAttributeCollection;
    }

    /**
     * @param categoryAttributeCollection the categoryAttributeCollection to set
     */
    public void setCategoryAttributeCollection(Set<CategoryAttribute> categoryAttributeCollection) 
    {
        this.categoryAttributeCollection = categoryAttributeCollection;
    }

    /**
     * @hibernate.set cascade="all"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CategoryEntity"
     * @hibernate.collection-key column="CHILD_CATEGORY_ENTITY_ID"     
     * @return the childCategories
     */
    public Set<CategoryEntity> getChildCategories() 
    {
        return childCategories;
    }

    /**
     * @param childCategories the childCategories to set
     */
    public void setChildCategories(Set<CategoryEntity> childCategories) 
    {
        this.childCategories = childCategories;
    }

    
    /**
     * @hibernate.many-to-one column="ENTITY_ID" cascade="save-update" unique="true"
     * @return the entity
     */
    public Entity getEntity() 
    {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) 
    {
        this.entity = entity;
    }

    /**
     * @return the pathFromParent
     */
    public Path getPathFromParent() {
        return pathFromParent;
    }

    /**
     * @param pathFromParent the pathFromParent to set
     */
    public void setPathFromParent(Path pathFromParent) {
        this.pathFromParent = pathFromParent;
    }
    
}