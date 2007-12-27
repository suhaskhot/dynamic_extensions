package edu.common.dynamicextensions.domain;

import java.util.Collection;
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

    /**
     *
     */
    protected Integer numberOfEntries;

    /**
     *
     */
    protected Collection<CategoryEntity> childCategories = new HashSet<CategoryEntity>();

    /**
     *
     */
    protected Collection<CategoryAttribute> categoryAttributeCollection = new HashSet<CategoryAttribute>();

    /**
     *
     */
    protected Entity entity;

   /**
    *
    */
    protected Collection<Path> pathCollection;


    /**
     *
     *
     */
    public CategoryEntity()
    {
    	super();

    }
    /**
	 * This method returns the number of entries.
	 * @hibernate.property name="numberOfEntries" type="integer" column="NUMBER_OF_ENTRIES"
	 * @return the maximum cardinality.
	 */
    public Integer getNumberOfEntries() {
        return numberOfEntries;
    }

    /**
     * @param noOfEntries the noOfEntries to set
     */
    public void setNumberOfEntries(Integer numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }


    /**
     * @hibernate.set name="categoryAttributeCollection" table="DYEXTN_CATEGORY_ATTRIBUTE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CategoryAttribute"
     * @return the categoryAttributeCollection
     */

    public Collection<CategoryAttribute> getCategoryAttributeCollection()
    {
        return categoryAttributeCollection;
    }

    /**
     * @param categoryAttributeCollection the categoryAttributeCollection to set
     */
    public void setCategoryAttributeCollection(Collection<CategoryAttribute> categoryAttributeCollection)
    {
        this.categoryAttributeCollection = categoryAttributeCollection;
    }

    /**
     * @hibernate.set name="childCategories" table="DYEXTN_CATEGORY_ENTITY"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CHILD_CATEGORY_ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.CategoryEntity"
     * @return the childCategories
     */
    public Collection<CategoryEntity> getChildCategories()
    {
        return childCategories;
    }

    /**
     * @param childCategories the childCategories to set
     */
    public void setChildCategories(Collection<CategoryEntity> childCategories)
    {
        this.childCategories = childCategories;
    }


    /**
     * @hibernate.many-to-one column="ENTITY_ID" cascade="save-update"
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
     * @hibernate.set name="pathCollection" table="DYEXTN_PATH"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Path"
     */
	private Collection<Path> getPathCollection()
	{
		return pathCollection;
	}

	/**
	 *
	 * @param pathCollection
	 */
	private void setPathCollection(Collection<Path> pathCollection)
	{
		this.pathCollection = pathCollection;
	}



}