
package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.AssociationTreeObject;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author rajesh_patil
 *
 */
public class EntityGroupManager extends AbstractMetadataManager implements EntityGroupManagerInterface, EntityGroupManagerConstantsInterface
{
	private static EntityGroupManagerInterface entityGroupManager = null;
	/**
	 * Static instance of the queryBuilder.
	 */
	private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

	/**
	 * Empty Constructor.
	 */
	protected EntityGroupManager()
	{
	}

	/**
	 * Returns the instance of the Entity Manager.
	 * @return entityManager singleton instance of the Entity Manager.
	 */
	public static synchronized EntityGroupManagerInterface getInstance()
	{
		if (entityGroupManager == null)
		{
			entityGroupManager = new EntityGroupManager();
			DynamicExtensionsUtility.initialiseApplicationVariables();
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
		}
		return entityGroupManager;
	}

	/**
	 * Save entity group which in turn saves the whole hierarchy.
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface persistEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroupInterface = (EntityGroupInterface) persistDynamicExtensionObject(group);

		//Update the dynamic extension cache for all containers within entitygroup
		DynamicExtensionsUtility.updateDynamicExtensionsCache(entityGroupInterface.getId());
		return entityGroupInterface;
	}

	/**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface persistEntityGroupMetadata(EntityGroupInterface entityGroup) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		addTaggedValue(entityGroup);
		EntityGroupInterface entityGroupInterface = (EntityGroupInterface) persistDynamicExtensionObjectMetdata(entityGroup);
		//Update the dynamic extension cache for all containers within entitygroup
		DynamicExtensionsUtility.updateDynamicExtensionsCache(entityGroupInterface.getId());
		return entityGroupInterface;
	}

	/**
	 * This method creates dynamic table queries for the entities within a group.
	 * @param group EntityGroup
	 * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
	 * @param hibernateDAO
	 * @param queryList List of queries to be executed to created dynamicn tables.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List reverseQueryList,
			HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		EntityGroupInterface entityGroup = (EntityGroupInterface) dynamicExtensionBaseDomainObject;
		getDynamicQueryList(addTaggedValue(entityGroup), reverseQueryList, hibernateDAO, queryList);
	}

	/**
	 * This method adds caB2BEntityGroup tagged value to the entity group
	 * @param entityGroup
	 * @return
	 */
	private EntityGroupInterface addTaggedValue(EntityGroupInterface entityGroup)
	{
		addTaggedValue(entityGroup, CAB2B_ENTITY_GROUP, CAB2B_ENTITY_GROUP);
		addTaggedValue(entityGroup, PACKAGE_NAME, entityGroup.getName());
		addTaggedValue(entityGroup, METADATA_ENTITY_GROUP, METADATA_ENTITY_GROUP);
		return entityGroup;
	}

	/**
	 * This method adds caB2BEntityGroup tagged value to the entity group
	 * @param entityGroup
	 * @return
	 */
	private EntityGroupInterface addTaggedValue(EntityGroupInterface entityGroup, String key, String value)
	{
		Collection<TaggedValueInterface> taggedValueCollection = entityGroup.getTaggedValueCollection();
		boolean isTaggedValueAdded = false;
		for (TaggedValueInterface taggedValue : taggedValueCollection)
		{
			if (taggedValue.getKey().equalsIgnoreCase(key))
			{
				isTaggedValueAdded = true;
			}
		}
		if (!isTaggedValueAdded)
		{
			TaggedValueInterface taggedValueInterface = DomainObjectFactory.getInstance().createTaggedValue();
			taggedValueInterface.setKey(key);
			taggedValueInterface.setValue(value);
			entityGroup.addTaggedValue(taggedValueInterface);
		}
		return entityGroup;
	}

	/**
	 * This method executes dynamic table queries created for all the entities within a group.
	 * @param queryList List of queries to be executed to created dynamicn tables.
	 * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
	 * @param rollbackQueryStack Stack to undo any changes done beforehand at DB level.
	 * @throws DynamicExtensionsSystemException
	 */
	protected void postProcess(List queryList, List reverseQueryList, Stack rollbackQueryStack, HibernateDAO hibernateDAO)
			throws DynamicExtensionsSystemException
	{
		queryBuilder.executeQueries(queryList, reverseQueryList, rollbackQueryStack, hibernateDAO);
	}

	/**
	 * This method is called when exception occurs while executing the rollback queries
	 * or reverse queries. When this method is called , it signifies that the database state
	 * and the metadata state for the entity are not in synchronisation and administrator
	 * needs some database correction.
	 * @param e The exception that took place.
	 * @param entity Entity for which data tables are out of sync.
	 */
	protected void LogFatalError(Exception e, AbstractMetadataInterface abstractMetadata)
	{
		String table = "";
		String name = "";
		if (abstractMetadata != null)
		{
			EntityGroupInterface entityGroup = (EntityGroupInterface) abstractMetadata;
			name = entityGroup.getName();
		}
		Logger.out.error("***Fatal Error.. Incosistent data table and metadata information for the entity -" + name + "***");
		Logger.out.error("The cause of the exception is - " + e.getMessage());
		Logger.out.error("The detailed log is : ");
		e.printStackTrace();
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getEntityGroupByShortName(java.lang.String)
	 */
	public EntityGroupInterface getEntityGroupByShortName(String entityGroupShortName) throws DynamicExtensionsSystemException
	{
		EntityGroupInterface entityGroupInterface = null;
		Collection entityGroupCollection = new HashSet();
		if (entityGroupShortName == null || entityGroupShortName.equals(""))
		{
			return entityGroupInterface;
		}
		//Getting the instance of the default biz logic class which has the method that returns the particular object
		//depending on the value of a particular column of the associated table.
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();

		try
		{
			//Calling retrieve method to  get the entity group object based on the given value of short name.
			//Passed parameters are the class name of the entity group class, the name of the hibernate object member variable
			// and the value of that member variable.
			entityGroupCollection = defaultBizLogic.retrieve(EntityGroup.class.getName(), "shortName", entityGroupShortName);
			if (entityGroupCollection != null && entityGroupCollection.size() > 0)
			{
				entityGroupInterface = (EntityGroupInterface) entityGroupCollection.iterator().next();
			}
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return entityGroupInterface;

	}

	/**
	 * This method returns the EntityInterface given the entity name.
	 * @param entityGroupShortName
	 * @return
	 */
	public EntityGroupInterface getEntityGroupByName(String entityGroupName) throws DynamicExtensionsSystemException
	{
		EntityGroupInterface entityGroupInterface = (EntityGroupInterface) getObjectByName(EntityGroup.class.getName(), entityGroupName);
		return entityGroupInterface;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getMainContainer(java.lang.Long)
	 */
	public Collection<NameValueBean> getMainContainer(Long entityGroupIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Map<String, HQLPlaceHolderObject> substitutionParameterMap = new HashMap<String, HQLPlaceHolderObject>();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("long", entityGroupIdentifier));
		return executeHQL("getMainContainers", substitutionParameterMap);
	}

	/**
	 * Returns all entitiy groups in the whole system
	 * @return Collection Entity group Beans Collection
	 * @throws DynamicExtensionsSystemException
	 */
	public Collection<NameValueBean> getAllEntityGroupBeans() throws DynamicExtensionsSystemException
	{

		Collection<NameValueBean> entityGroupBeansCollection = new ArrayList<NameValueBean>();
		Collection groupBeansCollection = executeHQL("getAllGroupBeans", new HashMap());
		Iterator groupBeansIterator = groupBeansCollection.iterator();
		Object[] objectArray;

		while (groupBeansIterator.hasNext())
		{
			objectArray = (Object[]) groupBeansIterator.next();
			NameValueBean entityGroupNameValue = new NameValueBean();
			entityGroupNameValue.setName(objectArray[0]);
			entityGroupNameValue.setValue(objectArray[1]);
			entityGroupBeansCollection.add(entityGroupNameValue);
		}

		return entityGroupBeansCollection;
	}

	/**
	 *
	 * @param entityGroupInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<AssociationTreeObject> getAssociationTree() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Collection associationTreeObjectCollection = new HashSet();

		Collection groupBeansCollection = getAllEntityGroupBeans();
		Iterator groupBeansIterator = groupBeansCollection.iterator();
		AssociationTreeObject associationTreeObject;

		while (groupBeansIterator.hasNext())
		{
			associationTreeObject = processGroupBean((NameValueBean) groupBeansIterator.next());
			associationTreeObjectCollection.add(associationTreeObject);
		}

		return associationTreeObjectCollection;
	}

	/**
	 *
	 * @param objectArray
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private AssociationTreeObject processGroupBean(NameValueBean groupBean) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AssociationTreeObject associationTreeObjectForGroup = new AssociationTreeObject(new Long(groupBean.getValue()), groupBean.getName());

		Map substitutionParameterMap = new HashMap();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("long", associationTreeObjectForGroup.getId()));

		Collection containersBeansCollection = executeHQL("getAllContainersBeansByEntityGroupId", substitutionParameterMap);

		Iterator containerBeansIterator = containersBeansCollection.iterator();
		Object[] objectArrayForContainerBeans;
		AssociationTreeObject associationTreeObjectForContainer;

		while (containerBeansIterator.hasNext())
		{
			objectArrayForContainerBeans = (Object[]) containerBeansIterator.next();
			associationTreeObjectForContainer = new AssociationTreeObject((Long) objectArrayForContainerBeans[0],
					(String) objectArrayForContainerBeans[1]);
			//processForChildContainer(associationTreeObjectForContainer);
			associationTreeObjectForGroup.addAssociationTreeObject(associationTreeObjectForContainer);

		}

		return associationTreeObjectForGroup;
	}

	/**
	 * @param entity
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void checkParentChangeAllowed(Entity entity) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		String tableName = entity.getTableProperties().getName();
		if (queryBuilder.isDataPresent(tableName))
		{
			throw new DynamicExtensionsApplicationException("Can not change the data type of the attribute", null, DYEXTN_A_010);
		}
	}

	/**
	 * validateEntityGroup.
	 * @throws DynamicExtensionsSystemException
	 */
	public boolean validateEntityGroup(EntityGroupInterface entityGroup) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		EntityGroup databaseCopyEntityGroup = (EntityGroup) DBUtil.loadCleanObj(EntityGroup.class, entityGroup.getId());
		Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
		for (EntityInterface entityInterface : entityCollection)
		{
			Entity entity = (Entity) entityInterface;
			if (entity.getId() == null)
			{
				DynamicExtensionsUtility.validateEntity(entity);
			}
			else
			{
				EntityInterface databaseCopy = getEntityFromGroup(databaseCopyEntityGroup, entity.getId());
				if (queryBuilder.isParentChanged((Entity) entity, (Entity) databaseCopy))
				{
					checkParentChangeAllowed(entity);
				}
			}
		}
		return true;
	}

	/**
	 * getEntityFromGroup.
	 * @param entityGroup
	 * @param entityId
	 * @return
	 */
	private EntityInterface getEntityFromGroup(EntityGroupInterface entityGroup, Long entityId)
	{
		Collection<EntityInterface> entityCollection = entityGroup.getEntityCollection();
		for (EntityInterface entityInterface : entityCollection)
		{
			if (entityInterface.getId() != null && entityInterface.getId().equals(entityId))
			{
				return entityInterface;
			}
		}
		return null;
	}

	/**
	 *
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
	}

	/**
	 * This method checks if the entity group can be created with the given name or not.
	 * @param entityGroup
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public void checkForDuplicateEntityGroupName(EntityGroupInterface entityGroup) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			dao.openSession(null);
			String query = "select count(*) from dyextn_abstract_metadata d , dyextn_entity_group e where d.identifier = e.identifier and d.name = '"
					+ entityGroup.getName() + "'";
			List result = dao.executeQuery(query, new SessionDataBean(), false, null);

			if (result != null && !result.isEmpty())
			{
				List count = (List) result.get(0);
				if (count != null && !count.isEmpty())
				{
					int numberOfOccurence = new Integer((String) count.get(0)).intValue();
					if (numberOfOccurence > 0)
					{
						throw new DynamicExtensionsApplicationException("Duplicate Entity Group name", null, DYEXTN_A_015);
					}
				}
			}
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while checking for duplicate group", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Class not found", e);
		}
		finally
		{
			try
			{
				dao.closeSession();
			} catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("DAOException", null);
			}
		}
	}
}
