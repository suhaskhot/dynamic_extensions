
package edu.common.dynamicextensions.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;

/**
 * Entites have attributes that distinguishes them form other entities.
 * This Class represnts the Attribute of Entities. 
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_PRIMITIVE_ATTRIBUTE" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public abstract class Attribute extends AbstractAttribute implements AttributeInterface
{

	/**
	 * Specifies whether this primitive attribute is a collection or not.
	 */
	protected Boolean isCollection;

	/**
	 * Specifies whether this is an identified field or not.
	 */
	protected Boolean isIdentified;

	/**
	 * Specifies whether this is a primary key.
	 */
	protected Boolean isPrimaryKey = new Boolean(false);

	/**
	 * Specifies whether the column is nullable or not
	 */
	protected Boolean isNullable = new Boolean(true);

	/**
	 * Column property associated to this primitive attribute.
	 */
	protected Collection<ColumnPropertiesInterface> columnPropertiesCollection;

	/**
	 * The Collection of the DataElements that represents the Pre-defined values of the Attribute.
	 */
	protected Collection<DataElementInterface> dataElementCollection;

	/**
	 * Empty constructor.
	 */
	public Attribute()
	{
	}

	/**
	 * This method returns whether the Attribute is a Collection or not.
	 * @hibernate.property name="isCollection" type="boolean" column="IS_COLLECTION" 
	 * @return whether the Attribute is a Collection or not.
	 */
	public Boolean getIsCollection()
	{
		return isCollection;
	}

	/**
	 * This method sets whether the Attribute is a Collection or not.
	 * @param isCollection the Boolean value to be set.
	 */
	public void setIsCollection(Boolean isCollection)
	{
		this.isCollection = isCollection;
	}

	/**
	 * This method returns whether the Attribute is identifiable or not.
	 * @hibernate.property name="isIdentified" type="boolean" column="IS_IDENTIFIED" 
	 * @return whether the Attribute is identifiable or not.
	 */
	public Boolean getIsIdentified()
	{
		return isIdentified;
	}

	/**
	 * This method sets whether the Attribute is identifiable or not.
	 * @param isIdentified the Boolean value to be set.
	 */
	public void setIsIdentified(Boolean isIdentified)
	{
		this.isIdentified = isIdentified;
	}

	/**
	 * This method retunrs whehter the Attribute is a primary key or not.
	 * @hibernate.property name="isPrimaryKey" type="boolean" column="IS_PRIMARY_KEY" 
	 * @return whehter the Attribute is a primary key or not.
	 */
	public Boolean getIsPrimaryKey()
	{
		return isPrimaryKey;
	}

	/**
	 * This method sets whehter the Attribute is a primary key or not.
	 * @param isPrimaryKey the Boolean value to be set.
	 */
	public void setIsPrimaryKey(Boolean isPrimaryKey)
	{
		this.isPrimaryKey = isPrimaryKey;
	}

	/**
	 * This method returns the Collection of Column Properties of the Attribute.
	 * @hibernate.set name="columnPropertiesCollection" table="DYEXTN_COLUMN_PROPERTIES"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PRIMITIVE_ATTRIBUTE_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties"
	 * @return the Collection of Column Properties of the Attribute.
	 */
	private Collection<ColumnPropertiesInterface> getColumnPropertiesCollection()
	{
		return columnPropertiesCollection;
	}

	/**
	 * This method sets the columnPropertiesCollection to given Collection of the ColumnProperties.
	 * @param columnPropertiesCollection the Collection of the ColumnProperties to be set.
	 */
	private void setColumnPropertiesCollection(Collection<ColumnPropertiesInterface> columnPropertiesCollection)
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
		if (columnPropertiesCollection != null)
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
		this.columnPropertiesCollection.add(columnProperties);
	}

	/**
	 * This method returns the Collection of the DataElements of the Attribute.
	 * @hibernate.set name="dataElementCollection" table="DYEXTN_DATA_ELEMENT"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="PRIMITIVE_ATTRIBUTE_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.DataElement"   
	 * @return the Collection of the DataElements of the Attribute.
	 */
	private Collection<DataElementInterface> getDataElementCollection()
	{
		return dataElementCollection;
	}

	/**
	 * This method sets the dataElementCollection to the given Collection of the DataElement.
	 * @param dataElementCollection the Collection of the DataElemnet to be set.
	 */
	private void setDataElementCollection(Collection<DataElementInterface> dataElementCollection)
	{
		this.dataElementCollection = dataElementCollection;
	}

	/**
	 * This method returns the DataElement associated with the Attribute. 
	 * @return the DataElement associated with the Attribute.
	 */
	public DataElementInterface getDataElement()
	{
		DataElementInterface dataElement = null;
		if (dataElementCollection != null)
		{
			Iterator dataElementIterator = dataElementCollection.iterator();
			dataElement = (DataElementInterface) dataElementIterator.next();
		}
		return dataElement;
	}

	/**
	 * This method sets the DataElement of the Attribute.
	 * @param dataElement the DataElement to be set.
	 */
	public void setDataElement(DataElementInterface dataElement)
	{
		if (dataElementCollection == null)
		{
			dataElementCollection = new HashSet<DataElementInterface>();
		}
		this.dataElementCollection.add(dataElement);
	}

	/**
	 * This method returns whether the Attribute is nullable or not.
	 * @hibernate.property name="isNullable" type="boolean" column="IS_NULLABLE" 
	 * @return whether the Attribute is nullable or not.
	 */
	public Boolean getIsNullable()
	{
		return isNullable;
	}

	/**
	 * This method sets the whether the Attribute is nullable or not.
	 * @param isNullable the Boolean value to be set.
	 */
	public void setIsNullable(Boolean isNullable)
	{
		this.isNullable = isNullable;
	}

}