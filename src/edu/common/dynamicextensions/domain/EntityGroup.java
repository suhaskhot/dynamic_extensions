package edu.common.dynamicextensions.domain;

import java.util.Collection;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_ENTITY_GROUP"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class EntityGroup extends AbstractMetadata  implements java.io.Serializable {

    private static final long serialVersionUID = 1234567890L;
    /**
     * Collection of entity in this entity group.
     */
	protected Collection entityCollection;
     
    /**
     * 
     *
     */
	public EntityGroup(){

	}
	
    /**
     * @hibernate.set name="entityCollection" table="DYEXTN_ENTITY_GROUP_REL" 
     * cascade="none" inverse="false" lazy="false"
     * @hibernate.collection-key column="ENTITY_GROUP_ID"
     * @hibernate.collection-many-to-many class="edu.common.dynamicextensions.domain.Entity" column="ENTITY_ID"
     * @return Returns the entityCollection.
     */
    public Collection getEntityCollection() {
        return entityCollection;
    }
    /**
     * @param entityCollection The entityCollection to set.
     */
    public void setEntityCollection(Collection entityCollection) {
        this.entityCollection = entityCollection;
    }
    
  
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}