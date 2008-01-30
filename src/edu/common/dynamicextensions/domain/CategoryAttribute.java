
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 *
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class CategoryAttribute extends BaseAbstractAttribute implements CategoryAttributeInterface, AttributeMetadataInterface
{

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
	protected Set<RuleInterface> ruleCollection = new HashSet<RuleInterface>();

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
	protected Collection<PermissibleValueInterface> defaultPermissibleValuesCollection = new HashSet<PermissibleValueInterface>();

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
	private void setColumnPropertiesCollection(Set<ColumnPropertiesInterface> columnPropertiesCollection)
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
	 * @hibernate.collection-key column="CATEGORY_ATTRIBUTE_ID"
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
			if (dataElementIterator.hasNext())
			{
				return (DataElement) dataElementIterator.next();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param dataElementInterface
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
	public Set<RuleInterface> getRuleCollection()
	{
		if (ruleCollection == null)
		{
			this.attribute.getRuleCollection();
		}
		return ruleCollection;
	}

	/**
	 * @param ruleCollection the ruleCollection to set
	 */
	public void setRuleCollection(Set<RuleInterface> ruleCollection)
	{
		this.ruleCollection = ruleCollection;
	}

	/**
	 * @hibernate.many-to-one column="ATTRIBUTE_ID" cascade="save-update" class="edu.common.dynamicextensions.domain.Attribute"
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

	/**
	 * @hibernate.set name="defaultPermissibleValuesCollection" table="DYEXTN_PERMISSIBLE_VALUE"
	 * cascade="all-delete-orphan" inverse="false" lazy="false"
	 * @hibernate.collection-key column="CATEGORY_ATTRIBUTE_ID"
	 * @hibernate.cache usage="read-write"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.PermissibleValue"   
	 * @return Returns the dataElementCollection.
	 */
	private Collection<PermissibleValueInterface> getDefaultPermissibleValuesCollection()
	{
		return defaultPermissibleValuesCollection;
	}

	/**
	 * @param defaultPermissibleValuesCollection the defaultPermissibleValuesCollection to set
	 */
	private void setDefaultPermissibleValuesCollection(Collection<PermissibleValueInterface> defaultPermissibleValuesCollection)
	{
		this.defaultPermissibleValuesCollection = defaultPermissibleValuesCollection;
	}

	/**
	 * This method return the default value for the category attribute if set otherwise 
	 * return the default value for the original atttribute
	 * @return
	 */
	public String getDefaultValue()
	{
		if (defaultPermissibleValuesCollection != null && !defaultPermissibleValuesCollection.isEmpty())
		{
			Iterator<PermissibleValueInterface> dataElementIterator = defaultPermissibleValuesCollection.iterator();
			return String.valueOf(dataElementIterator.next().getValueAsObject());
		}
		else
		{
			return ((AttributeMetadataInterface) this.attribute).getDefaultValue();
		}
	}

	/**
	 * @param sourceEntity
	 */
	public void setDefaultValue(PermissibleValueInterface permissibleValueInterface)
	{
		if (defaultPermissibleValuesCollection == null)
		{
			defaultPermissibleValuesCollection = new HashSet<PermissibleValueInterface>();
		}

		this.defaultPermissibleValuesCollection.add(permissibleValueInterface);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface#getMaxSize()
	 */
	public int getMaxSize()
	{
		return ((AttributeMetadataInterface) this.attribute).getMaxSize();
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface#getMeasurementUnit()
	 */
	public String getMeasurementUnit()
	{
		return ((AttributeMetadataInterface) this.attribute).getMeasurementUnit();
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface#getDecimalPlaces()
	 */
	public int getDecimalPlaces()
	{
		return ((AttributeMetadataInterface) this.attribute).getDecimalPlaces();
	}

	public AttributeTypeInformationInterface getAttributeTypeInformation()
	{
		return ((AttributeMetadataInterface) this.attribute).getAttributeTypeInformation();
	}

}
