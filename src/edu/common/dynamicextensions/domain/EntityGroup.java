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
     * 
     */
	protected Collection entityCollection;
    /**
     * 
     */
	public Entity rootNodeEntity;
    /**
     * 
     */
	public Collection entityGroupCollection;
    /**
     * 
     *
     */
	public EntityGroup(){

	}

    /**
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