package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 *
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ENTITY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 *
 */
public class CategoryEntity extends AbstractEntity implements CategoryEntityInterface
{

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
    protected Collection<CategoryEntityInterface> childCategories = new HashSet<CategoryEntityInterface>();

    /**
     *
     */
    protected Collection<CategoryAttributeInterface> categoryAttributeCollection = new HashSet<CategoryAttributeInterface>();

    /**
     *
     */
    protected EntityInterface entity;

   /**
    *
    */
    protected Collection<PathInterface> pathCollection = new HashSet<PathInterface>();

    /**
    *
    */
   protected Collection<CategoryInterface> categoryCollection = new HashSet<CategoryInterface>();
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

    public Collection<CategoryAttributeInterface> getCategoryAttributeCollection()
    {
        return categoryAttributeCollection;
    }

    /**
     * @param categoryAttributeCollection the categoryAttributeCollection to set
     */
    public void setCategoryAttributeCollection(Collection<CategoryAttributeInterface> categoryAttributeCollection)
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
    public Collection<CategoryEntityInterface> getChildCategories()
    {
        return childCategories;
    }

    /**
     * @param childCategories the childCategories to set
     */
    public void setChildCategories(Collection<CategoryEntityInterface> childCategories)
    {
        this.childCategories = childCategories;
    }


    /**
     * @hibernate.many-to-one class="edu.common.dynamicextensions.domain.Entity" column="ENTITY_ID" cascade="save-update"
     * @return the entity
     */
    public EntityInterface getEntity()
    {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(EntityInterface entity)
    {
        this.entity = entity;
    }

    /**
     * @hibernate.set name="pathCollection" table="DYEXTN_PATH"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Path"
     * @return the pathCollection
     */
	private Collection<PathInterface> getPathCollection()
	{
		return pathCollection;
	}

	/**
	 *
	 * @param pathCollection
	 */
	private void setPathCollection(Collection<PathInterface> pathCollection)
	{
		this.pathCollection = pathCollection;
	}
	/**
	 *
	 */
	public void addCategoryAttribute(CategoryAttributeInterface categoryAttributeInterface)
	{

	}
	/**
	 *
	 */
	public void removeCategoryAttribute(CategoryAttributeInterface categoryAttributeInterface)
	{

	}
	/**
	 *
	 */
	public void addPath(PathInterface pathInterface)
	{

	}
	/**
	 *
	 */
	public void removePath(PathInterface pathInterface)
	{

	}
	/**
	 *
	 */
	public void removeAllPaths()
	{

	}
	public void removeAllCategoryAttributes()
	{
		// TODO Auto-generated method stub

	}
    /**
     * @hibernate.set name="categoryCollection" table="DYEXTN_CATEGORY"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ENTITY_ID"
	 * @hibernate.cache  usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Category"
     * @return the categoryCollection
     */
	public Collection<CategoryInterface> getCategoryCollection()
	{
		return categoryCollection;
	}
	/**
	 *
	 * @param categoryCollection
	 */
	public void setCategoryCollection(Collection<CategoryInterface> categoryCollection)
	{
		this.categoryCollection = categoryCollection;
	}
	/**
	 *
	 */
	public CategoryInterface getCategory()
	{
		CategoryInterface categoryInterface = null;
		if (categoryCollection != null && !categoryCollection.isEmpty())
		{
			Iterator categoryCollectionIterator = categoryCollection.iterator();
			categoryInterface = (CategoryInterface) categoryCollectionIterator.next();
		}
		return categoryInterface;
	}
	/**
	 *
	 */
	public void setCategory(CategoryInterface categoryInterface)
	{
		if (categoryCollection == null)
		{
			categoryCollection = new HashSet<CategoryInterface>();
		}
		else
		{
			categoryCollection.clear();
		}
		this.categoryCollection.add(categoryInterface);
	}



}