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
     * the root node entity of this entity group.
     */
	public Entity rootNodeEntity;
    /**
     * Entity group collection in this entity. 
     */
	public Collection entityGroupCollection;
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
    
    //TODO Needs to be changed.
    /**
     * @hibernate.set name="entityGroupCollection" table="DYEXTN_ENTITY_GROUP"
     * cascade="none" inverse="true" lazy="false"
     * @hibernate.collection-key column="ENTITY_GROUP_ID"
     * @hibernate.collection-one-to-many class="edu.common.dynamicextensions.domain.EntityGroup"
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
     * @hibernate.many-to-one column ="ROOT_NODE_ENTITY_ID" class="edu.common.dynamicextensions.domain.Entity"
     * @return Returns the rootNodeEntity.
     */
    public Entity getRootNodeEntity() {
        return rootNodeEntity;
    }
    /**
     * @param rootNodeEntity The rootNodeEntity to set.
     */
    public void setRootNodeEntity(Entity rootNodeEntity) {
        this.rootNodeEntity = rootNodeEntity;
    }

	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
}