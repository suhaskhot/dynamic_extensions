package edu.common.dynamicextensions.domain;

import java.util.Collection;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 */
public class EntityGroup extends AbstractMetadata {

	protected Collection entityCollection;
	public Entity rootNodeEntity;
	public Collection entityGroupCollection;

	public EntityGroup(){

	}

	public void finalize() throws Throwable {
		super.finalize();
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
}