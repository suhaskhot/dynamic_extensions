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
public class Entity extends AbstractMetadata  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567890L;
	/**
	 * 
	 * 
	 */
	protected Collection attributeCollection;
	/**
	 * 
	 */
	protected TableProperties tableProperties;
	/**
	 * 
	 */
	protected Collection entityGroupCollection;
	/**
     * 
	 *
	 */
	public Entity(){
		
	}
	
	/**
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
	
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}