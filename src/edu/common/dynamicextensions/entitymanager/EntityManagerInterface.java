
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.AssociationTreeObject;

/**
 * The methods only work on domain object interfaces and also return domain object interfaces or
 * collection of domain object interfaces.
 * 
 * These methods may throw  DynamicExtensionsApplicationException or DynamicExtensionsSystemException.
 * 
 * DynamicExtensionsApplicationException are application specific exceptions and system can recover from those 
 * exceptions.
 * 
 * DynamicExtensionsSystemException are system specific exceptions and system can not recover from those
 * exception.An error page should be displayed in the case.
 * @author sujay_narkar
 *
 */
public interface EntityManagerInterface
{

	/**
	 * Saves the entity into the database.Also prepares the dynamic tables and associations 
	 * between those tables using the metadata information in the entity object.
	 * EntityInterface can be obtained from DomainObjectFactory.
	 * @param entityInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityInterface persistEntity(EntityInterface entityInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method creates an entity group.The entities in the group are also saved.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group. 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityGroupInterface persistEntityGroup(EntityGroupInterface entityGroupInterface)
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

	/**
	 * @param entityGroupName
	 * @return EntityGroupInterface EntityGroupInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityGroupInterface getEntityGroupByName(String entityGroupName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns an attribute given the entity name and attribute name.
	 * @param entityName
	 * @param attributeName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	AttributeInterface getAttribute(String entityName, String attributeName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns an association object given the entity name and source role name.
	 * @param entityName
	 * @param sourceRoleName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<AssociationInterface> getAssociation(String entityName, String sourceRoleName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entities given the entity concept code.
	 * @param entityConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByConceptCode(String entityConceptCode)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns all entities in the whole system
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getAllEntities() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns a single  entity for given identifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityInterface getEntityByIdentifier(String identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns the entity based on the id passed.
	 * @param id
	 * @return
	 * @throws DynamicExtensionsSystemException thrown in case of fatal system exceptions
	 * @throws DynamicExtensionsApplicationException thrown in case application specific errors.
	 */
	EntityInterface getEntityByIdentifier(Long id) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entities having attribute with the given name  
	 * @param attributeName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByAttributeName(String attributeName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns all Containers in the whole system
	 * @return Collection of ContainerInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<ContainerInterface> getAllContainers() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entity objects given the entity description
	 * @param entityDescription
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntityByDescription(String entityDescription)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of Entity objects given the attribute description
	 * @param attributeDescription
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByAttributeDescription(String attributeDescription)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entity objects given the entity concept name.
	 * @param entityConceptName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByConceptName(String entityConceptName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entities given attribute concept code. 
	 * @param attributeConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByAttributeConceptCode(String attributeConceptCode)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entities given the attribute concept name
	 * @param attributeConceptname
	 * @return 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByAttributeConceptName(String attributeConceptName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entity objects given the entity object with specific criteria. 
	 * @param entityInterface
	 * @return
	 */
	Collection<EntityInterface> findEntity(EntityInterface entityInterface);

	/**
	 * This method is used to save the container into the database.
	 * @param containerInterface container to save
	 * @return ContainerInterface container Interface that is saved.
	 * @throws DynamicExtensionsSystemException Thrown if for any reason operation can not be completed.
	 * @throws DynamicExtensionsApplicationException Thrown if the entity name already exists.
	 * @throws DynamicExtensionsSystemException 
	 */
	ContainerInterface persistContainer(ContainerInterface containerInterface)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * This method inserts one record for the entity.
	 */
	Long insertData(EntityInterface entity, Map<AbstractAttributeInterface, ?> dataValue)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * Returns a particular record for the given recordId of the given entityId
	 * @param entityId
	 * @param recordId
	 * @return Map key - attribute name 
	 *             value - attribute value  
	 */
	Map<AbstractAttributeInterface, Object> getRecordById(EntityInterface entity, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param attribute
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public FileAttributeRecordValue getFileAttributeRecordValueByRecordId(AttributeInterface attribute,
			Long recordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method deletes a particular record for an entity.
	 * @param entity Entity for which record needs to be deleted
	 * @param recordId Id of the record
	 * @return success failure flag. true if successful
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	boolean deleteRecord(EntityInterface entity, Long recordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * This method updates the existing record for the given entity.
	 * @param entity       Entity for which record needs to be updated
	 * @param dataValue    map that contains  name of the attribute whose value is changed and its new value
	 *                     If it is multiselect attribute then valu should be List<string>
	 * @param recordId     Id of the record
	 * @return true if success
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	boolean editData(EntityInterface entity, Map<AbstractAttributeInterface, ?> dataValue,
			Long recordId) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException;

	/**
	 * Returns all entityGroups in the system.
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityGroupInterface> getAllEntitiyGroups() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method returns collection of all the containers of the entity contained within a 
	 * entity group.
	 * 
	 * @param entityGroupIdentifier id of entity group 
	 * @return collection of the containers
	 */
	Collection<ContainerInterface> getAllContainersByEntityGroupId(Long entityGroupIdentifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method is used to save the metadata information of the given entity without creating it's data 
	 * table.
	 * 
	 * @param entityInterface entity to be persisted
	 * @param isDataTablePresent boolean indicating if data table is already existing or not
	 */
	EntityInterface persistEntityMetadata(EntityInterface entityInterface,
			boolean isDataTablePresent) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

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
	 * This method returns container interface given the container identifier
	 * @param identifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	ContainerInterface getContainerByIdentifier(String identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method returns the all the record for the given control of the 
	 * association.
	 * 
	 * @return map 
	 *    key   recordId
	 *    value List<String> list of column values 
	 */
	Map<Long, List<String>> getRecordsForAssociationControl(
			AssociationControlInterface associationControl) throws DynamicExtensionsSystemException;

	/**
	 * This method returns the container interface given the entity identifier.
	 * @param EntityInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	ContainerInterface getContainerByEntityIdentifier(Long entityIdentifier)
			throws DynamicExtensionsSystemException;

	/**
	 * This method returns the control given the attribute identifier
	 * @param controlIdentifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	ControlInterface getControlByAbstractAttributeIdentifier(Long abstractAttributeIdentifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * 
	 * @param entityGroupInterface
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 */
	Collection<AssociationTreeObject> getAssociationTree() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * returns all the records for a given entity
	 */
	List<EntityRecord> getAllRecords(EntityInterface entity)
			throws DynamicExtensionsSystemException;

	/**
	 * This method returns all the children entities of givens entity.
	 * @return Collection of EntityInterface
	 * @throws DynamicExtensionsSystemException 
	 */
	Collection<EntityInterface> getChildrenEntities(EntityInterface entity)
			throws DynamicExtensionsSystemException;

	/**
	 * @param associationId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	AssociationInterface getAssociationByIdentifier(Long associationId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method returns collection of the association for a given target entity.
	 * @param entity
	 * @return
	 */
	Collection<AssociationInterface> getIncomingAssociations(EntityInterface entity)
			throws DynamicExtensionsSystemException;
	
	/**
	 * @param entityGroupIdentifier
	 * @return
	 */
	ContainerInterface getMainContainer(Long entityGroupIdentifier)  throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
}
