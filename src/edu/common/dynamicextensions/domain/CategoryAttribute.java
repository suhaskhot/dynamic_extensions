package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domain.userinterface.Control;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.CaDSRValueDomainInfoInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 *
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class CategoryAttribute extends BaseAbstractAttribute implements CategoryAttributeInterface,AttributeInterface {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 12345235L;
    /**
     *
     */
    protected Set<DataElementInterface> dataElementCollection = new HashSet<DataElementInterface>();
    /**
     *
     */
    protected Set<ColumnPropertiesInterface> columnPropertiesCollection = new HashSet<ColumnPropertiesInterface>();
    /**
     *
     */
    protected Set<RuleInterface> ruleCollection =  new HashSet<RuleInterface>();
    /**
     *
     */
    protected AttributeInterface attribute;
    /**
     *
     */
    protected CategoryEntityInterface categoryEntity;
    /**
    *
    */
   protected Collection<CategoryEntityInterface> categoryEntityCollection = new HashSet<CategoryEntityInterface>();

    public CategoryAttribute()
    {
        super();
    }

    /**
     * This method returns the Collection of Column Properties of the Attribute.
     * @hibernate.set name="columnPropertiesCollection" table="DYEXTN_COLUMN_PROPERTIES" cascade="all" inverse="false" lazy="false"
     * @hibernate.collection-key column="CATEGORY_ATTRIBUTE_ID"
     * @hibernate.cache usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties"
     * @return the Collection of Column Properties of the Attribute.
     */
    private Set<ColumnPropertiesInterface> getColumnPropertiesCollection()
    {
        return columnPropertiesCollection;
    }

    /**
     * This method sets the columnPropertiesCollection to given Collection of the ColumnProperties.
     * @param columnPropertiesCollection the Collection of the ColumnProperties to be set.
     */
    private void setColumnPropertiesCollection(
            Set<ColumnPropertiesInterface> columnPropertiesCollection)
    {
        this.columnPropertiesCollection = columnPropertiesCollection;
    }

    /**
     * This method returns the ColumnProperties of the Attribute.
     * @return the ColumnProperties of the Attribute.
     */
    public ColumnPropertiesInterface getColumnProperties()
    {
        ColumnPropertiesInterface columnProperties = null;
        if (columnPropertiesCollection != null && !columnPropertiesCollection.isEmpty())
        {
            Iterator columnPropertiesIterator = columnPropertiesCollection.iterator();
            columnProperties = (ColumnPropertiesInterface) columnPropertiesIterator.next();
        }
        return columnProperties;
    }

    /**
     * This method sets the ColumnProperties of the Attribute.
     * @param columnProperties the ColumnProperties to be set.
     */
    public void setColumnProperties(ColumnPropertiesInterface columnProperties)
    {
        if (columnPropertiesCollection == null)
        {
            columnPropertiesCollection = new HashSet<ColumnPropertiesInterface>();
        }
        else
        {
            columnPropertiesCollection.clear();
        }
        this.columnPropertiesCollection.add(columnProperties);
    }

    /**
     * @hibernate.set name="dataElementCollection" table="DYEXTN_DATA_ELEMENT" cascade="all" inverse="false" lazy="false"
     * @hibernate.collection-key column="CATEGORY_ATTRIBUTE_TYPE_INFO_ID"
     * @hibernate.cache  usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.DataElement"
     * @return Returns the dataElementCollection.
     */
    private Set<DataElementInterface> getDataElementCollection()
    {
        return dataElementCollection;
    }

    /**
     * @param dataElementCollection The dataElementCollection to set.
     */
    private void setDataElementCollection(Set<DataElementInterface> dataElementCollection)
    {
        this.dataElementCollection = dataElementCollection;
    }

    /**
     *
     * @return
     */
    public DataElementInterface getDataElement()
    {
        if (dataElementCollection != null)
        {
            Iterator dataElementIterator = dataElementCollection.iterator();
            if(dataElementIterator.hasNext())
            {
                return (DataElement) dataElementIterator.next();
            }
        }
        return null;
    }

    /**
     *
     * @param sourceEntity
     */
    public void setDataElement(DataElementInterface dataElementInterface)
    {
        if (dataElementCollection == null)
        {
            dataElementCollection = new HashSet();
        }
        this.dataElementCollection.add(dataElementInterface);
    }

    /**
     * @hibernate.set name="ruleCollection" table="DYEXTN_RULE" cascade="all-delete-orphan" inverse="false" lazy="false"
     * @hibernate.collection-key column="CATEGORY_ATTRIBUTE_ID"
     * @hibernate.cache usage="read-write"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.validationrules.Rule"
     * @return the ruleCollection
     */
    public Set<RuleInterface> getRuleCollection() {
        return ruleCollection;
    }

    /**
     * @param ruleCollection the ruleCollection to set
     */
    public void setRuleCollection(Set<RuleInterface> ruleCollection) {
        this.ruleCollection = ruleCollection;
    }

    /**
     * @hibernate.many-to-one column="ATTRIBUTE_ID" cascade="save-update" unique="true" class="edu.common.dynamicextensions.domain.Attribute"
     * @return the attribute
     */
    public AttributeInterface getAttribute()
    {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(AttributeInterface attribute)
    {
        this.attribute = attribute;
    }

	public AttributeTypeInformationInterface getAttributeTypeInformation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public CaDSRValueDomainInfoInterface getCaDSRValueDomainInfo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getDataType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getIsCollection()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getIsIdentified()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getIsNullable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getIsPrimaryKey()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttributeTypeInformation(AttributeTypeInformationInterface attributeTypeInformationInterface)
	{
		// TODO Auto-generated method stub

	}

	public void setCaDSRValueDomainInfo(CaDSRValueDomainInfoInterface caDSRValueDomainInfoInterface)
	{
		// TODO Auto-generated method stub

	}

	public void setIsCollection(Boolean isCollection)
	{
		// TODO Auto-generated method stub

	}

	public void setIsIdentified(Boolean isIdentified)
	{
		// TODO Auto-generated method stub

	}

	public void setIsNullable(Boolean isNullable)
	{
		// TODO Auto-generated method stub

	}

	public void setIsPrimaryKey(Boolean isPrimaryKey)
	{
		// TODO Auto-generated method stub

	}

	public void addRule(RuleInterface ruleInterface)
	{
		// TODO Auto-generated method stub

	}

	public Collection<Control> getControl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public EntityInterface getEntity()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void removeRule(RuleInterface ruleInterface)
	{
		// TODO Auto-generated method stub

	}

	public void setControl(Collection<Control> control)
	{
		// TODO Auto-generated method stub

	}

	public void setEntity(EntityInterface entityInterface)
	{
		// TODO Auto-generated method stub

	}

	public void setRuleCollection(Collection<RuleInterface> ruleCollection)
	{
		// TODO Auto-generated method stub

	}
	/**
	 * This method returns the Entity associated with this Attribute.
	 * @hibernate.many-to-one column="CATEGORY_ENTIY_ID" class="edu.common.dynamicextensions.domain.CategoryEntity" constrained="true"
	 * @return CategoryEntityInterface the Entity associated with the Attribute.
	 */
	public CategoryEntityInterface getCategoryEntity()
	{
		return categoryEntity;
	}

	/**
	 * This method sets the Entity associated with this Attribute.
	 * @param entityInterface The entity to be set.
	 */
	public void setCategoryEntity(CategoryEntityInterface categoryEntityInterface)
	{
		if (categoryEntityInterface != null)
		{
			this.categoryEntity = categoryEntityInterface;
		}
	}

}
