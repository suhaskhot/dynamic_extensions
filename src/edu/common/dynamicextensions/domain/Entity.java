package edu.common.dynamicextensions.domain;
import java.util.Collection;

import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * An entity can either have many attributes or many associations
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"  
 */
public class Entity extends AbstractMetadata {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 * Collection of attributes in this entity.
	 */
	protected Collection attributeCollection;
	/**
	 * Table property for this entity.
	 */
	protected TableProperties tableProperties;
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
     * cascade="none" inverse="false" lazy="false"
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
	/**
	 * @hibernate.many-to-one column ="TABLE_PROPERTY_ID" class="edu.common.dynamicextensions.domain.databaseproperties.TableProperties"
	 * @return Returns the tableProperties.
	 */
	public TableProperties getTableProperties() {
		return tableProperties;
	}
	/**
	 * @param tableProperties The tableProperties to set.
	 */
	public void setTableProperties(TableProperties tableProperties) {
		this.tableProperties = tableProperties;
	}
	
	public void setAllValues(AbstractActionForm arg0)  {
		// TODO Auto-generated method stub
		
	}
}