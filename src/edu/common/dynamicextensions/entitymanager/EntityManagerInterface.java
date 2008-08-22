
package edu.common.dynamicextensions.entitymanager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
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
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 *
 * @author mandar_shidhore
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
	public EntityInterface persistEntity(EntityInterface entityInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method is used to save the metadata information of the given entity without creating it's data
	 * table.
	 *
	 * @param entityInterface entity to be persisted
	 */
	public EntityInterface persistEntityMetadata(EntityInterface entity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of association objects given the source entity id and
	 * target entity id.
	 * @param sourceEntityId
	 * @param targetEntityId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns an entity object given the entity name;
	 * @param entityName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityInterface getEntityByName(String entityName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns an association object given the entity name and source role name.
	 * @param entityName
	 * @param sourceRoleName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<AssociationInterface> getAssociation(String entityName, String sourceRoleName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 *
	 * @param sourceEntityName
	 * @param sourceRoleName
	 * @param targetEntityName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	AssociationInterface getAssociation(String sourceEntityName, String sourceRoleName, String targetEntityName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	AssociationInterface getAssociationByName(String associationName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a collection of entities given the entity concept code.
	 * @param entityConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getEntitiesByConceptCode(String entityConceptCode) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns all entities in the whole system
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<EntityInterface> getAllEntities() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns a single  entity for given identifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityInterface getEntityByIdentifier(String identifier) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns the entity based on the id passed.
	 * @param id
	 * @return
	 * @throws DynamicExtensionsSystemException thrown in case of fatal system exceptions
	 * @throws DynamicExtensionsApplicationException thrown in case application specific errors.
	 */
	EntityInterface getEntityByIdentifier(Long id) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Returns all Containers in the whole system
	 * @return Collection of ContainerInterface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	Collection<ContainerInterface> getAllContainers() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * Retrieve all containers from a particular group.
	 * @param entityGroupIdentifier
	 * @return collection of containers.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<ContainerInterface> getAllContainersByEntityGroupId(Long entityGroupIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method inserts one record for the entity.
	 */
	Long insertData(EntityInterface entity, Map<AbstractAttributeInterface, Object> dataValue, Long... userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * This method inserts multiple records for the entity. This is a single transaction, so either all records are inserted or nothing
	 * is persisted.
	 * It returns the record id in the same sequence as that of input maps in dataValueMapList.
	 */
	List<Long> insertData(EntityInterface entity, List<Map<AbstractAttributeInterface, Object>> dataValueMapList, Long... userId)
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
	boolean editData(EntityInterface entity, Map<AbstractAttributeInterface, ?> dataValue, Long recordId, Long... userId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * @param entity
	 * @param abstractAttributeCollection
	 * @param recordIdList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	EntityRecordResultInterface getEntityRecords(EntityInterface entity, List<? extends AbstractAttributeInterface> abstractAttributeCollection,
			List<Long> recordIdList) throws DynamicExtensionsSystemException;

	/**
	 * Returns a particular record for the given recordId of the given entityId
	 * @param entityId
	 * @param recordId
	 * @return Map key - attribute name
	 *             value - attribute value
	 */
	Map<AbstractAttributeInterface, Object> getRecordById(EntityInterface entity, Long recordId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method deletes a particular record for an entity.
	 * @param entity Entity for which record needs to be deleted
	 * @param recordId Id of the record
	 * @return success failure flag. true if successful
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	boolean deleteRecord(EntityInterface entity, Long recordId) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	 * Method deletes the passed records of the passed container.
	 * @param containerId
	 * @param recordIdList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	void deleteRecords(Long containerId, List<Long> recordIdList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method returns the all the record for the given control of the
	 * association.
	 *
	 * @return map
	 *    key   recordId
	 *    value List<String> list of column values
	 */
	Map<Long, List<String>> getRecordsForAssociationControl(AssociationControlInterface associationControl) throws DynamicExtensionsSystemException;

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	List<NameValueBean> getAllContainerBeans() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 *
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public List<ContainerInformationObject> getAllContainerInformationObjects() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	*
	* @return
	* @throws DynamicExtensionsSystemException
	* @throws DynamicExtensionsApplicationException
	*/
	public Map<String, String> getAllContainerBeansMap() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * returns all the records for a given entity
	 */
	List<EntityRecord> getAllRecords(AbstractEntityInterface entity) throws DynamicExtensionsSystemException;

	/**
	 * This method returns all the children entities of givens entity.
	 * @return Collection of EntityInterface
	 * @throws DynamicExtensionsSystemException
	 */
	Collection<EntityInterface> getChildrenEntities(EntityInterface entity) throws DynamicExtensionsSystemException;

	/**
	 * @param associationId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	AssociationInterface getAssociationByIdentifier(Long associationId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method returns collection of the association for a given target entity.
	 * @param entity
	 * @return
	 */
	Collection<AssociationInterface> getIncomingAssociations(EntityInterface entity) throws DynamicExtensionsSystemException;

	/**
	 * @param containerId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	String getContainerCaption(Long containerId) throws DynamicExtensionsSystemException;

	/**
	 * @param association
	 * @throws DynamicExtensionsSystemException
	 */
	void addAssociationColumn(AssociationInterface association) throws DynamicExtensionsSystemException;

	/**
	 * @param associationInterface
	 * @param sourceEntityRecordId
	 * @param TargetEntityRecordId
	 * @throws DynamicExtensionsSystemException
	 */
	void associateEntityRecords(AssociationInterface associationInterface, Long sourceEntityRecordId, Long TargetEntityRecordId)
			throws DynamicExtensionsSystemException;

	/**
	 * @param containerId
	 * @throws DynamicExtensionsSystemException
	 */
	Long getEntityIdByContainerId(Long containerId) throws DynamicExtensionsSystemException;

	Map<Long, Date> getEntityCreatedDateByContainerId() throws DynamicExtensionsSystemException;

	/**
	*
	* @param isAbstarct
	* @param entityIdentifier
	* @return
	* @throws DynamicExtensionsSystemException
	*/
	public Long checkContainerForAbstractEntity(Long entityIdentifier, boolean isAbstarct) throws DynamicExtensionsSystemException;

	/**
	 * @param entityIdentifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public Long checkContainerForAbstractCategoryEntity(Long entityIdentifier) throws DynamicExtensionsSystemException;

	/**
	 * Get entity id for entity. This method fires direct SQL query and does not use hibernate for
	 * performance reasons.
	 *
	 * @param entityName : Name of the entity for which Id is to be fetched
	 * @return :  Id of the specified entity
	 * @throws DynamicExtensionsSystemException
	 */
	public Long getEntityId(String entityName) throws DynamicExtensionsSystemException;

	/**
	 * Get the container Id for the specified entity Id
	 * This method fires direct JDBC SQL queries without using hibernate for performance purposes
	 * @param entityId : Id for the entity whose container id is to be fetched
	 * @return : container Id for specified entity
	 * @throws DynamicExtensionsSystemException
	 */
	public Long getContainerIdForEntity(Long entityId) throws DynamicExtensionsSystemException;

	/**
	 * Get next identifier for an entity from entity table when a record is to be inserted to the entity table.
	 * @param entityName :  Name of the entity
	 * @return :  Next identifier that can be assigned to a entity record
	 * @throws DynamicExtensionsSystemException
	 */
	public Long getNextIdentifierForEntity(String entityName) throws DynamicExtensionsSystemException;

	/**
	 * Returns an attribute given the entity name and attribute name.
	 * @param entityName name of the entity.
	 * @param attributeName name of the attribute.
	 * @return AttributeInterface attribute interface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public AttributeInterface getAttribute(String entityName, String attributeName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * Returns all entitiy groups in the whole system
	 * @return Collection Entity group Beans Collection
	 * @throws DynamicExtensionsSystemException
	 */
	public Collection<NameValueBean> getAllEntityGroupBeans() throws DynamicExtensionsSystemException;

	/**
	 * validateEntityGroup.
	 * @throws DynamicExtensionsSystemException
	 */
	public boolean validateEntity(EntityInterface entity) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

	/**
	*
	* @param entityId
	* @param attributeId
	* @return
	* @throws DynamicExtensionsSystemException
	*/
	public Collection<Integer> getAttributeRecordsCount(Long entityId, Long attributeId) throws DynamicExtensionsSystemException;

	/**
	 * This method returns the container interface given the entity identifier.
	 * @param EntityInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public ContainerInterface getContainerByEntityIdentifier(Long entityIdentifier) throws DynamicExtensionsSystemException;

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<AssociationTreeObject> getAssociationTree() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * @param attribute
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public FileAttributeRecordValue getFileAttributeRecordValueByRecordId(AttributeInterface attribute, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	public Long isCategory(Long containerId) throws DynamicExtensionsSystemException;

	/**
	*
	* @param hookEntityId
	* @return the container Id of the DE entities that are associated with given static hook entity
	*/
	public Collection<ContainerInterface> getDynamicEntitiesContainerIdFromHookEntity(Long hookEntityId) throws DynamicExtensionsSystemException;

	/**
	*
	* @param hookEntityId
	* @return  the container Id of the DE entities/categories that are associated with given static hook entity
	* @throws DynamicExtensionsSystemException
	*/
	public Collection<ContainerInterface> getCategoriesContainerIdFromHookEntity(Long hookEntityId) throws DynamicExtensionsSystemException;

	/**
	*
	* @param hookEntityRecId
	* @param containerId
	* @return the category or form record id based on the containerId and hookentityRecId
	*/
	public String getDynamicTableName(Long containerId) throws DynamicExtensionsSystemException;

	/**
	*
	* @param categoryContainerId
	* @param staticRecId
	* @return the record id of the category depending on hook entity record id.
	*/
	public String getColumnNameForAssociation(Long hookEntityId, Long containerId) throws DynamicExtensionsSystemException;

	public Long getCategoryRootContainerId(Long containerId) throws DynamicExtensionsSystemException;

	/**
	* @param entityGroupIdentifier
	* @return
	*/
	Collection<NameValueBean> getMainContainer(Long entityGroupIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	* @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#persistEntityMetadata(edu.common.dynamicextensions.domaininterface.EntityInterface)
	*/
	public EntityInterface persistEntityMetadataForAnnotation(EntityInterface entityInterface, boolean isDataTablePresent,
			boolean copyDataTableState, AssociationInterface association) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	* @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getAssociationsForTargetEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
	*/
	public Collection<Long> getIncomingAssociationIds(EntityInterface entity) throws DynamicExtensionsSystemException;

	/**
	* @param entityGroupName
	* @return EntityGroupInterface EntityGroupInterface
	* @throws DynamicExtensionsSystemException
	* @throws DynamicExtensionsApplicationException
	*/
	EntityGroupInterface getEntityGroupByName(String entityGroupName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	* This method returns the EntityGroupInterface given the short name for the
	* entity.
	* @param entityGroupShortName short name for entity group
	* @return entityGroupInterface entity group interface
	* @throws DynamicExtensionsSystemException
	*/
	EntityGroupInterface getEntityGroupByShortName(String entityGroupShortName) throws DynamicExtensionsSystemException;

	/**
	* Returns all entityGroups in the system.
	* @return
	* @throws DynamicExtensionsSystemException
	* @throws DynamicExtensionsApplicationException
	*/
	Collection<EntityGroupInterface> getAllEntitiyGroups() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method returns the control given the attribute identifier
	 * @param controlIdentifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public ControlInterface getControlByAbstractAttributeIdentifier(Long abstractAttributeIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * @param entityGroupId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public List<NameValueBean> getAllContainerBeansByEntityGroupId(Long entityGroupId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	public Map<AbstractAttributeInterface, Object> getEntityRecordById(EntityInterface entity, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * @param entity
	 * @param dataValue
	 * @param hibernateDAO
	 * @param parentRecordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	public Long insertDataForSingleEntity(EntityInterface entity, Map dataValue, HibernateDAO hibernateDAO, Long parentRecordId, Long... userId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, HibernateException, SQLException, DAOException,
			UserNotAuthorizedException;

	/**
	 * @param entity
	 * @param dataValue
	 * @param recordId
	 * @param hibernateDAO
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	public boolean editDataForSingleEntity(EntityInterface entity, Map dataValue, Long recordId, HibernateDAO hibernateDAO, Long... userId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, HibernateException, SQLException, DAOException,
			UserNotAuthorizedException;

	/**
	 * @param entity
	 * @param dataValue
	 * @param hibernateDAO
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	public Long insertDataForHeirarchy(EntityInterface entity, Map<AbstractAttributeInterface, ?> dataValue, HibernateDAO hibernateDAO,
			Long... userId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, HibernateException, SQLException,
			DAOException, UserNotAuthorizedException;

	/**
	 * @param entity
	 * @param dataValue
	 * @param recordId
	 * @param hibernateDAO
	 * @param userId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public boolean editDataForHeirarchy(EntityInterface entity, Map<AbstractAttributeInterface, ?> dataValue, Long recordId,
			HibernateDAO hibernateDAO, Long... userId) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

}
