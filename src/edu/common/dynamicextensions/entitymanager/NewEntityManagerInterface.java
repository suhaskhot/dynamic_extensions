package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 *
 * @author mandar_shidhore
 *
 */
public interface NewEntityManagerInterface {

    public void saveEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Saves the entity into the database.Also prepares the dynamic tables and associations
	 * between those tables using the metadata information in the entity object.
	 * EntityInterface can be obtained from DomainObjectFactory.
	 * @param entityInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityInterface persistEntity(EntityInterface entityInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method is used to save the metadata information of the given entity without creating it's data
	 * table.
	 *
	 * @param entityInterface entity to be persisted
	 */
	public EntityInterface persistEntityMetadata(EntityInterface entity)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method returns the EntityGroupInterface given the short name for the
	 * entity.
	 * @param entityGroupShortName short name for entity group
	 * @return entityGroupInterface entity group interface
	 * @throws DynamicExtensionsSystemException
	 */
	EntityGroupInterface getEntityGroupByShortName(String entityGroupShortName)
			throws DynamicExtensionsSystemException;

	/**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityGroupInterface persistEntityGroupMetadata(EntityGroupInterface entityGroupInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
    /**
     * Returns a collection of association objects given the source entity id and
     * target entity id.
     * @param sourceEntityId
     * @param targetEntityId
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
    /**
     * Returns an entity object given the entity name;
     * @param entityName
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    EntityInterface getEntityByName(String entityName) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException;
}
