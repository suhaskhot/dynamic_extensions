package edu.common.dynamicextensions.domain;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * An entity can either have many attributes or many associations
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class Entity extends AbstractMetadata implements EntityInterface {
	
	/**
	 * Collection of attributes in this entity.
	 */
	protected Collection attributeCollection = new HashSet();
	/**
	 * Table property for this entity.
	 */
	protected Collection tablePropertiesCollection;
	/**
	 * 
	 */
	protected Collection entityGroupCollection;
	/**
	 * empty Constructor.
	 */
	public Entity(){
		
	}
	
	/**
	 * @hibernate.set name="attributeCollection" table="DYEXTN_ATTRIBUTE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="ATTRIBUTE_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.Attribute"
	 * @return Returns the attributeCollection.
	 */
	public Collection getAttributeCollection() {
		return attributeCollection;
	}
	/**
	 * @param attributeCollection The attributeCollection to set.
	 */
	public void setAttributeCollection(Collection attributeCollection) {
		this.attributeCollection = attributeCollection;
	}
	/**
	 * @hibernate.set name="entityGroupCollection" table="DYEXTN_ENTITY_GROUP_REL" 
	 * cascade="none" inverse="true" lazy="false"
	 * @hibernate.collection-key column="ENTITY_ID"
	 * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.EntityGroup" column="ENTITY_GROUP_ID"
	 * @return Returns the entityGroupCollection.
	 */
	public Collection getEntityGroupCollection() {
		return entityGroupCollection;
	}
	/**
	 * @param entityGroupCollection The entityGroupCollection to set.
	 */
	public void setEntityGroupCollection(Collection entityGroupCollection) {
		this.entityGroupCollection = entityGroupCollection;
	}
	
	
	public void setAllValues(AbstractActionForm arg0)  {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @hibernate.set name="tablePropertiesColletion" table="DYEXTN_TABLE_PROPERTIES" cascade="save-update"
	 * inverse="false" lazy="false"
	 * @hibernate.collection-key column="ENTITY_ID"
	 * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.databaseproperties.TableProperties"
	 * @return Returns the tablePropertiesColletion.
	 */
	private Collection getTablePropertiesCollection() {
		return tablePropertiesCollection;
	}
	/**
	 * @param tablePropertiesColletion The tablePropertiesColletion to set.
	 */
	private void setTablePropertiesCollection(Collection tablePropertiesColletion) {
		this.tablePropertiesCollection = tablePropertiesColletion;
	}
    
    
    /**
     * 
     * @return
     */
    public TableProperties getTableProperties(){
        if(tablePropertiesCollection != null){
            Iterator tabletPropertiesIterator = tablePropertiesCollection.iterator();
            return (TableProperties)tabletPropertiesIterator.next();
        } else {
            return null;   
        }
        
    }
    
    /**
     * 
     * @param sourceEntity
     */
    public void setTableProperties(TableProperties tableProperties){
        if(tablePropertiesCollection == null){
            tablePropertiesCollection  = new HashSet();
        }
        this.tablePropertiesCollection .add(tableProperties);
    }

    /**
     * 
     */
	public void addAttribute(AttributeInterface attributeInterface) {
		attributeCollection.add(attributeInterface);		
	}

    /**
     * 
     */
	public void addEntityGroupInterface(EntityGroupInterface entityGroupInterface) {
		// TODO Auto-generated method stub
		
	}
}