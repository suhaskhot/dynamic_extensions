package edu.common.dynamicextensions.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 * 
 * @author mandar_shidhore
 * @hibernate.joined-subclass table="DYEXTN_CATEGORY_ATTRIBUTE"
 * @hibernate.joined-subclass-key column="id"
 */
public class CategoryAttribute extends BaseAbstractAttribute {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 12345235L;
    
    protected Set<DataElementInterface> dataElementCollection = new HashSet<DataElementInterface>();
    
    protected Set<ColumnPropertiesInterface> columnPropertiesCollection = new HashSet<ColumnPropertiesInterface>();
    
    protected Set<RuleInterface> ruleCollection =  new HashSet<RuleInterface>();
    
    protected AttributeInterface attribute;
    
    public CategoryAttribute()
    {
        
    }
   
    /**
     * This method returns the Collection of Column Properties of the Attribute.
     * @hibernate.set name="columnPropertiesCollection" table="DYEXTN_COLUMN_PROPERTIES" cascade="all" inverse="false" lazy="false"
     * @hibernate.collection-key column="PRIMITIVE_ATTRIBUTE_ID"
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
     * @hibernate.collection-key column="ATTRIBUTE_TYPE_INFO_ID"
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
     * @hibernate.collection-key column="ATTRIBUTE_ID"
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
     * @hibernate.many-to-one cascade="none" unique="true" class="edu.common.dynamicextensions.domain.Attribute"
     * @return the attribute
     */
    public AttributeInterface getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(AttributeInterface attribute) {
        this.attribute = attribute;
    }

}
