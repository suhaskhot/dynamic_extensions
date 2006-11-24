
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.CollectionAttributeRecord;
import edu.common.dynamicextensions.domain.CollectionAttributeRecordValue;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.BaseDynamicExtensionsException;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.util.global.Constants.AssociationDirection;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * This is a singleton class that manages operations related to dynamic entity creation,attributes creation,
 * adding data into those entities and retrieving data out of them.
 * 
 *  In order to mock  EntityManager class we need to create a a mock class which extends EntityManager class.
 * i.e.We create a class named as EntityManagerMock which will extend from EntityManager.EntityManagerMock 
 * class will override the unimplemented methods from EntityManager.Entity manager is having a method 
 * as setInstance.The application which is using this mock will place the instance of mock class in
 * EntityManager class using setInstancxe method on startup.  
 * 
 *    
 * @author Geetika Bangard
 * @author Vishvesh Mulay
 * @author Rahul Ner
 */
public class EntityManager
		implements
			EntityManagerInterface,
			EntityManagerConstantsInterface,
			EntityManagerExceptionConstantsInterface
{

	/**
	 * Static instance of the entity manager.
	 */
	private static EntityManagerInterface entityManagerInterface = null;

	/**
	 * Empty Constructor.
	 */
	protected EntityManager()
	{
	}

	/**
	 * Returns the instance of the Entity Manager.
	 * @return entityManager singleton instance of the Entity Manager.
	 */
	public static synchronized EntityManagerInterface getInstance()
	{
		if (entityManagerInterface == null)
		{
			entityManagerInterface = new EntityManager();
			/*This is added for api purpose*/
			//TODO needs to chk for host application's log4j
			Logger.configure("");
		}

		DynamicExtensionsUtility.initialiseApplicationVariables();

		return entityManagerInterface;
	}

	/**
	 * Mock entity manager can be placed in the entity manager using this method.
	 * @param entityManager
	 */
	public void setInstance(EntityManagerInterface entityManagerInterface)
	{
		EntityManager.entityManagerInterface = entityManagerInterface;

	}

	/**
	 * 
	 * @param methodName
	 * @param message
	 */
	private void logDebug(String methodName, String message)
	{

		Logger.out.debug("[EntityManager.]" + methodName + "()--" + message);
		System.out.println("[EntityManager.]" + methodName + "()--" + message);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#persistEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
	 */
	public EntityInterface persistEntity(EntityInterface entityInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		logDebug("persistEntity", "entering the method");
		Entity entity = (Entity) entityInterface;
		boolean isEntitySaved = true;
		if (entity.getId() == null)
		{
			isEntitySaved = false;
		}

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		Stack stack = new Stack();

		try
		{
			hibernateDAO.openSession(null);

			entityInterface = saveOrUpdateEntity(entityInterface, hibernateDAO, stack,
					isEntitySaved);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			logDebug("persistEntity", DynamicExtensionsUtility.getStackTrace(e));
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}
		catch (BaseDynamicExtensionsException e)
		{
			rollbackQueries(stack, entity, e);

			if (e instanceof DynamicExtensionsApplicationException)
			{
				throw (DynamicExtensionsApplicationException) e;
			}

			if (e instanceof DynamicExtensionsSystemException)
			{
				throw (DynamicExtensionsSystemException) e;
			}
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				rollbackQueries(stack, entity, e);
			}
		}
		logDebug("persistEntity", "exiting the method");
		return entityInterface;
	}

	/**
	 * This method creates an entity group.The entities in the group are also saved.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group. 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface createEntityGroup(EntityGroupInterface entityGroupInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		logDebug("createEntityGroup", "Entering method");
		EntityGroup entityGroup = (EntityGroup) entityGroupInterface;
		preSaveProcessEntityGroup(entityGroup);
		entityGroup = saveOrUpdateEntityGroup(entityGroupInterface, true);
		logDebug("createEntity", "Exiting method");
		return entityGroupInterface;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getEntityGroupByShortName(java.lang.String)
	 */
	public EntityGroupInterface getEntityGroupByShortName(String entityGroupShortName)
			throws DynamicExtensionsSystemException
	{
		EntityGroupInterface entityGroupInterface = null;
		Collection entityGroupCollection = new HashSet();
		if (entityGroupShortName == null || entityGroupShortName.equals(""))
		{
			return entityGroupInterface;
		}
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();

		try
		{
			entityGroupCollection = defaultBizLogic.retrieve(EntityGroup.class.getName(),
					"shortName", entityGroupShortName);
			if (entityGroupCollection != null && entityGroupCollection.size() > 0)
			{
				entityGroupInterface = (EntityGroupInterface) entityGroupCollection.iterator()
						.next();
			}
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return entityGroupInterface;

	}
	

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getAssociations(java.lang.Long, java.lang.Long)
	 */
	private List<String> getCollectionAttributeRecordValues(Long entityId, Long attributeId,Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CollectionAttributeRecord collectionAttributeRecord = getCollectionAttributeRecord(entityId, attributeId, recordId);
		Collection <CollectionAttributeRecordValue>recordValueCollection = collectionAttributeRecord.getValueCollection();
		
		List<String> valueList = new ArrayList<String>();
		for(CollectionAttributeRecordValue recordValue :recordValueCollection) {
			valueList.add(recordValue.getValue());
		}
		return valueList;	
	}
	
	/**
	 * @param entityId
	 * @param attributeId
	 * @param recordId
	 * @return
	 */
	private CollectionAttributeRecord getCollectionAttributeRecord(Long entityId, Long attributeId,Long recordId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException{
	
		Map substitutionParameterMap = new HashMap();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("long", entityId));
		substitutionParameterMap.put("1", new HQLPlaceHolderObject("long", attributeId));
		substitutionParameterMap.put("2", new HQLPlaceHolderObject("long", recordId));
		

		Collection recordCollection = executeHQL("getCollectionAttributeRecord", substitutionParameterMap);
		CollectionAttributeRecord collectionAttributeRecord = (CollectionAttributeRecord) recordCollection.iterator().next();
		return collectionAttributeRecord;
	}
	

	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getAssociations(java.lang.Long, java.lang.Long)
	 */
	public Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map substitutionParameterMap = new HashMap();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("long", sourceEntityId));
		substitutionParameterMap.put("1", new HQLPlaceHolderObject("long", targetEntityId));

		Collection associationCollection = executeHQL("getAssociations", substitutionParameterMap);

		return associationCollection;
	}

	/**
	 * This method returns the EntityInterface given the entity name.
	 * @param entityGroupShortName
	 * @return
	 */
	public EntityInterface getEntityByName(String entityName)
			throws DynamicExtensionsSystemException
	{
		EntityInterface entityInterface = null;
		if (entityName == null || entityName.equals(""))
		{
			return entityInterface;
		}
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		List entityInterfaceList = new ArrayList();
		try
		{
			entityInterfaceList = defaultBizLogic.retrieve(Entity.class.getName(), "name",
					entityName);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		if (entityInterfaceList != null && entityInterfaceList.size() > 0)
		{
			entityInterface = (EntityInterface) entityInterfaceList.get(0);
		}

		return entityInterface;
	}

	/**
	 * Returns an attribute given the entity name and attribute name.
	 * @param entityName name of the entity.
	 * @param attributeName name of the attribute.
	 * @return AttributeInterface attribute interface
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public AttributeInterface getAttribute(String entityName, String attributeName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AttributeInterface attributeInterface = null;
		AbstractAttributeInterface abstractAttributeInterface;
		String name;
		if (entityName == null || entityName.equals("") || attributeName == null
				|| attributeName.equals(""))
		{
			return attributeInterface;
		}
		EntityInterface entityInterface = getEntityByName(entityName);
		if (entityInterface != null)
		{
			Collection abstractAttributeCollection = entityInterface
					.getAbstractAttributeCollection();
			if (abstractAttributeCollection != null)
			{
				Iterator abstractAttributeIterator = abstractAttributeCollection.iterator();

				while (abstractAttributeIterator.hasNext())
				{
					abstractAttributeInterface = (AbstractAttributeInterface) abstractAttributeIterator
							.next();
					if (abstractAttributeInterface instanceof AttributeInterface)
					{
						attributeInterface = (AttributeInterface) abstractAttributeInterface;
						name = attributeInterface.getName();
						if (name != null && name.equals(attributeName))
						{
							return attributeInterface;
						}
					}
				}

			}

		}
		return attributeInterface;
	}

	/**
	 * Returns an association object given the entity name and source role name.
	 * @param entityName
	 * @param sourceRoleName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */

	public AssociationInterface getAssociation(String entityName, String sourceRoleName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map substitutionParameterMap = new HashMap();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("string", entityName));
		substitutionParameterMap.put("1", new HQLPlaceHolderObject("string", sourceRoleName));

		Collection associationCollection = executeHQL("getAssociation", substitutionParameterMap);

		return (AssociationInterface) associationCollection.iterator().next();
	}

	/**
	 * This method returns the collection of entities given the concept code for the entity. 
	 * @param entityConceptCode concept code for the entity
	 * @return entityCollection a collection of entities.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<EntityInterface> getEntitiesByConceptCode(String entityConceptCode)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map substitutionParameterMap = new HashMap();
		substitutionParameterMap.put("0", new HQLPlaceHolderObject("string", entityConceptCode));
		Collection entityCollection = executeHQL("getEntitiesByConceptCode",
				substitutionParameterMap);
		return entityCollection;
	}

	/**
	 * Returns all entities in the whole system
	 * @return Collection Entity Collection
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection<EntityInterface> getAllEntities() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		return getAllObjects(EntityInterface.class.getName());
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getEntityByIdentifier(java.lang.String)
	 */
	public EntityInterface getEntityByIdentifier(String identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return (EntityInterface) getObjectByIdentifier(EntityInterface.class.getName(), identifier);
	}

	/**
	 * This method populates the TableProperties object in entity which holds the unique tablename for the entity. This 
	 * table name is generated using the unique identifier that is generated after saving the object. The format for generating 
	 * this table/column name is "DYEXTN_<ENTITY/ATTRIBUTE/ASSOCIATION>_<UNIQUE IDENTIFIER>"
	 * So we need this method to generate the table name for the entity then create the corresponding tableProperties 
	 * object and then update the entity object with this newly added tableProperties object. 
	 * Similarly we add ColumnProperties object for each of the attribute and also the ConstraintProperties object 
	 * for each of the associations.
	 * @param entity Entity object on which to process the post save operations.
	 * @param rollbackQueryStack 
	 * @param hibernateDAO 
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 * @throws UserNotAuthorizedException 
	 * @throws DAOException 
	 */
	private void postSaveProcessEntity(Entity entity, HibernateDAO hibernateDAO,
			Stack rollbackQueryStack) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, DAOException, UserNotAuthorizedException
	{
		if (entity.getTableProperties() == null)
		{
			TableProperties tableProperties = new TableProperties();
			String tableName = TABLE_NAME_PREFIX + UNDERSCORE + entity.getId();
			tableProperties.setName(tableName);
			entity.setTableProperties(tableProperties);
		}
		Collection attributeCollection = entity.getAbstractAttributeCollection();
		entity.setLastUpdated(new Date());
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Collection tempAttributeCollection = new HashSet(attributeCollection);
			Iterator iterator = tempAttributeCollection.iterator();
			while (iterator.hasNext())
			{
				AbstractAttribute attribute = (AbstractAttribute) iterator.next();
				if (attribute instanceof Attribute
						&& ((Attribute) attribute).getColumnProperties() == null)
				{
					ColumnProperties colProperties = new ColumnProperties();
					String colName = COLUMN_NAME_PREFIX + UNDERSCORE + attribute.getId();
					colProperties.setName(colName);
					((Attribute) attribute).setColumnProperties(colProperties);
				}
				else if (attribute instanceof AssociationInterface)
				{
					Association association = (Association) attribute;
					ConstraintPropertiesInterface constraintProperties = association
							.getConstraintProperties();
					//					if (constraintProperties == null)
					//					{
					EntityInterface targetEntity = association.getTargetEntity();
					if (targetEntity.getId() == null)
					{
						boolean isEntitySaved = false;
						targetEntity = saveOrUpdateEntity(targetEntity, hibernateDAO,
								rollbackQueryStack, isEntitySaved);
					}
					populateConstraintProperties(association);

					populateSystemGeneratedAssociation(association, hibernateDAO);
				}

				//				}

			}
		}
	}

	/**
	 * @param association
	 * @param hibernateDAO 
	 * @throws UserNotAuthorizedException 
	 * @throws DAOException 
	 */
	private void populateSystemGeneratedAssociation(Association association,
			HibernateDAO hibernateDAO) throws DAOException, UserNotAuthorizedException
	{
		Association systemGeneratedAssociation = getSystemGeneratedAssociation(association);
		if (association.getAssociationDirection() == AssociationDirection.BI_DIRECTIONAL)
		{
			ConstraintPropertiesInterface constraintPropertiesSysGen = new ConstraintProperties();
			if (systemGeneratedAssociation == null)
			{
				systemGeneratedAssociation = new Association();
			}
			else
			{
				constraintPropertiesSysGen = systemGeneratedAssociation.getConstraintProperties();
			}
			constraintPropertiesSysGen.setName(association.getConstraintProperties().getName());
			constraintPropertiesSysGen.setSourceEntityKey(association.getConstraintProperties()
					.getTargetEntityKey());
			constraintPropertiesSysGen.setTargetEntityKey(association.getConstraintProperties()
					.getSourceEntityKey());

			systemGeneratedAssociation.setName(association.getName());
			systemGeneratedAssociation.setDescription(association.getDescription());
			systemGeneratedAssociation.setTargetEntity(association.getEntity());
			systemGeneratedAssociation.setEntity(association.getTargetEntity());
			systemGeneratedAssociation.setSourceRole(association.getTargetRole());
			systemGeneratedAssociation.setTargetRole(association.getSourceRole());
			systemGeneratedAssociation.setAssociationDirection(AssociationDirection.BI_DIRECTIONAL);
			systemGeneratedAssociation.setIsSystemGenerated(true);
			systemGeneratedAssociation.setConstraintProperties(constraintPropertiesSysGen);

			association.getTargetEntity().addAbstractAttribute(systemGeneratedAssociation);
		}
		else
		{
			if (systemGeneratedAssociation != null)
			{
				association.getTargetEntity().removeAbstractAttribute(systemGeneratedAssociation);
			}
		}
		hibernateDAO.update(association.getTargetEntity(), null, false, false, false);
	}

	private Association getSystemGeneratedAssociation(Association association)
	{
		EntityInterface targetEnetity = association.getTargetEntity();
		Collection associationCollection = targetEnetity.getAssociationCollection();
		if (associationCollection != null && !associationCollection.isEmpty())
		{
			Iterator associationIterator = associationCollection.iterator();
			while (associationIterator.hasNext())
			{
				Association associationInterface = (Association) associationIterator.next();
				if (associationInterface.getIsSystemGenerated()
						&& associationInterface.getSourceRole().equals(association.getTargetRole())
						&& associationInterface.getTargetRole().equals(association.getSourceRole()))
				{
					return associationInterface;
				}
			}
		}
		return null;
	}

	private void populateConstraintProperties(Association association)
	{

		ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties();
		if (constraintProperties == null)
		{
			constraintProperties = DomainObjectFactory.getInstance().createConstraintProperties();
		}
		EntityInterface sourceEntity = association.getEntity();
		EntityInterface targetEntity = association.getTargetEntity();
		RoleInterface sourceRole = association.getSourceRole();
		RoleInterface targetRole = association.getTargetRole();
		Cardinality sourceMaxCardinality = sourceRole.getMaximumCardinality();
		Cardinality targetMaxCardinality = targetRole.getMaximumCardinality();
		if (sourceMaxCardinality == Cardinality.MANY && targetMaxCardinality == Cardinality.MANY)
		{
			constraintProperties.setSourceEntityKey(ASSOCIATION_COLUMN_PREFIX + UNDERSCORE + "S"
					+ UNDERSCORE + sourceEntity.getId() + UNDERSCORE + association.getId()
					+ UNDERSCORE + IDENTIFIER);
			constraintProperties.setTargetEntityKey(ASSOCIATION_COLUMN_PREFIX + UNDERSCORE + "T"
					+ UNDERSCORE + targetEntity.getId() + UNDERSCORE + association.getId()
					+ UNDERSCORE + IDENTIFIER);
			constraintProperties.setName(ASSOCIATION_NAME_PREFIX + UNDERSCORE
					+ sourceEntity.getId() + UNDERSCORE + targetEntity.getId() + UNDERSCORE
					+ +association.getId());
		}
		else if (sourceMaxCardinality == Cardinality.MANY
				&& targetMaxCardinality == Cardinality.ONE)
		{
			constraintProperties.setSourceEntityKey(ASSOCIATION_COLUMN_PREFIX + UNDERSCORE
					+ targetEntity.getId() + UNDERSCORE + association.getId() + UNDERSCORE
					+ IDENTIFIER);
			constraintProperties.setTargetEntityKey(null);
			constraintProperties.setName(null);
		}
		else
		{
			constraintProperties.setTargetEntityKey(ASSOCIATION_COLUMN_PREFIX + UNDERSCORE
					+ sourceEntity.getId() + UNDERSCORE + association.getId() + UNDERSCORE
					+ IDENTIFIER);
			constraintProperties.setSourceEntityKey(null);
			constraintProperties.setName(null);
		}
		association.setConstraintProperties(constraintProperties);

	}

	/**
	 * This method checks if the entity can be created with the given name or not. This method will check for the duplicate name 
	 * as per the following rule
	 * <br>The entities which belong to the same entity group can not share same name.
	 * @param entity Entity whose name's uniqueness is to be checked.
	 * @throws DynamicExtensionsApplicationException This will basically act as a duplicate name  exception.
	 */
	private void checkForDuplicateEntityName(Entity entity)
			throws DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * This method executes the queries which generate and or manipulate the data table associated with the entity.
	 * @param entity Entity for which the data table queries are to be executed.
	 * @param rollbackQueryStack 
	 * @param reverseQueryList2 
	 * @param queryList2 
	 * @param hibernateDAO 
	 * @param session Hibernate Session through which connection is obtained to fire the queries.
	 * @throws DynamicExtensionsSystemException Whenever there is any exception , this exception is thrown with proper message and the exception is 
	 * wrapped inside this exception.
	 */
	private Stack executeQueries(List queryList, List reverseQueryList, Stack rollbackQueryStack)
			throws DynamicExtensionsSystemException
	{
		Session session = null;
		try
		{
			session = DBUtil.currentSession();
		}
		catch (HibernateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new DynamicExtensionsSystemException(
					"Unable to exectute the data table queries .....Cannot access sesssion", e1,
					DYEXTN_S_002);
		}

		Iterator reverseQueryListIterator = reverseQueryList.iterator();

		try
		{
			Connection conn = session.connection();
			if (queryList != null && !queryList.isEmpty())
			{
				Iterator queryListIterator = queryList.iterator();
				while (queryListIterator.hasNext())
				{
					String query = (String) queryListIterator.next();
					System.out.println("Query: " + query);
					PreparedStatement statement = null;
					try
					{
						statement = conn.prepareStatement(query);
					}
					catch (SQLException e)
					{
						throw new DynamicExtensionsSystemException(
								"Exception occured while executing the data table query", e);
					}
					try
					{
						statement.executeUpdate();
						if (reverseQueryListIterator.hasNext())
						{
							rollbackQueryStack.push(reverseQueryListIterator.next());
						}
					}
					catch (SQLException e)
					{
						//                        rollbackQueries(rollbackQueryStack, conn, entity);
						throw new DynamicExtensionsSystemException(
								"Exception occured while forming the data tables for entity", e,
								DYEXTN_S_002);
					}
				}
			}
		}
		catch (HibernateException e)
		{
			throw new DynamicExtensionsSystemException(
					"Cannot obtain connection to execute the data query", e, DYEXTN_S_001);
		}

		return rollbackQueryStack;

	}

	/**
	 * This method is called when there any exception occurs while generating the data table queries for the entity. Valid scenario is 
	 * that if we need to fire Q1 Q2 and Q3 in order to create the data tables and Q1 Q2 get fired successfully and exception occurs 
	 * while executing query Q3 then this method receives the query list which holds the set of queries which negate the effect of 
	 * the queries which were generated successfully so that the metadata information and database are in synchronisation.
	 * @param reverseQueryList Stack that maintains the queries to execute. 
	 * @param conn
	 * @throws DynamicExtensionsSystemException
	 */
	private void rollbackQueries(Stack reverseQueryList, Entity entity, Exception e)
			throws DynamicExtensionsSystemException
	{
		String message = "";
		if (reverseQueryList != null && !reverseQueryList.isEmpty())
		{

			Connection conn;
			try
			{
				conn = DBUtil.getConnection();
				while (!reverseQueryList.empty())
				{
					String query = (String) reverseQueryList.pop();
					PreparedStatement statement = null;
					statement = conn.prepareStatement(query);
					statement.executeUpdate();
				}
			}
			catch (HibernateException e1)
			{
				message = e1.getMessage();

			}
			catch (SQLException exc)
			{
				message = exc.getMessage();
				LogFatalError(exc, entity);
			}
			finally
			{
				logDebug("rollbackQueries", DynamicExtensionsUtility.getStackTrace(e));
				DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(message,
						e);
				ex.setErrorCode(DYEXTN_S_000);
				throw ex;
			}

		}

	}

	/**
	 * this method is called when exception occurs while executing the rollback queries or reverse queries. When this method is called , it 
	 * signifies that the database state and the metadata state for the entity are not in synchronisation and administrator needs some 
	 * database correction.
	 * @param e The exception that took place.
	 * @param entity Entity for which data tables are out of sync.
	 */
	private void LogFatalError(Exception e, Entity entity)
	{
		Logger.out
				.error("***Fatal Error.. Incosistent data table and metadata information for the entity -"
						+ entity.getName());
		Logger.out.error("Please check the table -" + entity.getTableProperties().getName());
		Logger.out.error("The cause of the exception is - " + e.getMessage());
		Logger.out.error("The detailed log is : ");
		e.printStackTrace();

	}

	/**
	 * This method builds the list of all the queries that need to be executed in order to create the data table for the entity and its associations.
	 * @param entity Entity for which to get the queries.
	 * @param reverseQueryList For every data table query the method builds one more query which negats the effect of that data table query. All such
	 * reverse queries are added in this list.
	 * @param rollbackQueryStack 
	 * @param hibernateDAO 
	 * @return List of all the data table queries
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	private List getCreateEntityQueryList(Entity entity, List reverseQueryList,
			HibernateDAO hibernateDAO, Stack rollbackQueryStack)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List queryList = new ArrayList();
		String mainTableQuery = getEntityMainDataTableQuery(entity, reverseQueryList);
		List associationTableQueryList = getDataTableQueriesForAssociationsInEntity(entity,
				reverseQueryList, hibernateDAO, rollbackQueryStack);
		queryList.add(mainTableQuery);
		queryList.addAll(associationTableQueryList);
		return queryList;
	}

	/**
	 * This method returns all the CREATE table entries for associations present in the entity.
	 * @param entity Entity object from which to get the associations.
	 * @param reverseQueryList Reverse query list that holds the reverse queries.
	 * @param rollbackQueryStack 
	 * @param hibernateDAO 
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	private List getDataTableQueriesForAssociationsInEntity(Entity entity, List reverseQueryList,
			HibernateDAO hibernateDAO, Stack rollbackQueryStack)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List associationQueryList = new ArrayList();
		Collection associationCollection = entity.getAssociationCollection();
		if (associationCollection != null && !associationCollection.isEmpty())
		{
			Iterator associationIterator = associationCollection.iterator();
			while (associationIterator.hasNext())
			{
				AssociationInterface association = (AssociationInterface) associationIterator
						.next();
				if (((Association) association).getIsSystemGenerated())
				{
					continue;
				}
				boolean isAddAssociationQuery = true;
				String associationQuery = getQueryPartForAssociation(association, reverseQueryList,
						isAddAssociationQuery);
				associationQueryList.add(associationQuery);
			}
		}
		return associationQueryList;
	}

	/**
	 * This method returns the main data table CREATE query that is associated with the entity.
	 * @param entity Entity for which to create the data table query.
	 * @param reverseQueryList Reverse query list which holds the query to negate the data table query.
	 * @return String The method returns the "CREATE TABLE" query for the data table query for the entity passed.
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getEntityMainDataTableQuery(Entity entity, List reverseQueryList)
			throws DynamicExtensionsSystemException
	{
		String dataType = getDataTypeForIdentifier();
		String tableName = entity.getTableProperties().getName();
		StringBuffer query = new StringBuffer(CREATE_TABLE + " " + tableName + " "
				+ OPENING_BRACKET + " " + IDENTIFIER + " " + dataType + COMMA);
		Collection attributeCollection = entity.getAttributeCollection();
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();
			while (attributeIterator.hasNext())
			{
				Attribute attribute = (Attribute) attributeIterator.next();
                if (attribute.getIsCollection() != null && attribute.getIsCollection()) {
                    continue;
                }
				String attributeQueryPart = getQueryPartForAbstractAttribute(attribute, true);
				query = query.append(attributeQueryPart);
				query = query.append(COMMA);
			}
		}
		query = query.append(PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE + ")");
		String reverseQuery = getReverseQueryForEntityDataTable(entity);
		reverseQueryList.add(reverseQuery);
		return query.toString();
	}

	/**
	 * This method gives the opposite query to negate the effect of "CREATE TABLE" query for the data table for the entity. 
	 * @param entity Entity for which query generation is done.
	 * @return String query that basically holds the "DROP TABLE" query.
	 */
	private String getReverseQueryForEntityDataTable(Entity entity)
	{
		String query = null;
		if (entity != null && entity.getTableProperties() != null)
		{
			query = "Drop table" + " " + entity.getTableProperties().getName();
		}
		return query;
	}

	/**
	 * This method returns the query part for an individual abstract attribute. The abstract attribute can be a primitive
	 * attribute or association. so according to the type of the attribute appropriate method is called to get the query part of that abstract 
	 * attribute.
	 * @param attribute Abstract attribute that can be primitive or association. 
	 * @return String query part of that attribute.
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getQueryPartForAbstractAttribute(AbstractAttribute attribute,
			boolean processUniqueConstraint) throws DynamicExtensionsSystemException
	{
		String attributeQuery = null;
		if (attribute != null)
		{
			if (attribute instanceof Attribute)
			{
				try
				{
					attributeQuery = getQueryPartForAttribute((Attribute) attribute,
							processUniqueConstraint);
				}
				catch (DataTypeFactoryInitializationException e)
				{
					throw new DynamicExtensionsSystemException(
							"Exception occured while retrieving the database type of the attribute");
				}
			}
			else if (attribute instanceof Association)
			{
				//	attributeQuery = getQueryPartForAssociation((Association) attribute);
			}
		}
		return attributeQuery;
	}

	/**
	 * This method builds the query part for the association.
	 * @param association Association object for which to build the query.
	 * @param reverseQueryList 
	 * @return String query part of the association.
	 * @throws DynamicExtensionsSystemException 
	 */
	private String getQueryPartForAssociation(AssociationInterface association,
			List reverseQueryList, boolean isAddAssociationQuery)
			throws DynamicExtensionsSystemException
	{
		logDebug("getQueryPartForAssociation", "Entering method");

		StringBuffer query = new StringBuffer();
		EntityInterface sourceEntity = association.getEntity();
		EntityInterface targetEntity = association.getTargetEntity();
		RoleInterface sourceRole = association.getSourceRole();
		RoleInterface targetRole = association.getTargetRole();
		Cardinality sourceMaxCardinality = sourceRole.getMaximumCardinality();
		Cardinality targetMaxCardinality = targetRole.getMaximumCardinality();
		ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties();
		String tableName = "";

		String dataType = getDataTypeForIdentifier();
		if (sourceMaxCardinality == Cardinality.MANY && targetMaxCardinality == Cardinality.MANY)
		{
			tableName = constraintProperties.getName();

			query.append(CREATE_TABLE + WHITESPACE + tableName + WHITESPACE + OPENING_BRACKET
					+ WHITESPACE + IDENTIFIER + WHITESPACE + dataType + COMMA);
			query.append(constraintProperties.getSourceEntityKey() + WHITESPACE + dataType + COMMA);
			query.append(constraintProperties.getTargetEntityKey() + WHITESPACE + dataType + COMMA
					+ WHITESPACE);
			query.append(PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE + CLOSING_BRACKET);
			String rollbackQuery = DROP_KEYWORD + WHITESPACE + TABLE_KEYWORD + WHITESPACE
					+ tableName;

			if (isAddAssociationQuery)
			{
				reverseQueryList.add(rollbackQuery);
			}
			else
			{
				reverseQueryList.add(query.toString());
				query = new StringBuffer(rollbackQuery);
			}
		}
		else if (sourceMaxCardinality == Cardinality.MANY
				&& targetMaxCardinality == Cardinality.ONE)
		{
			tableName = sourceEntity.getTableProperties().getName();
			String columnName = constraintProperties.getSourceEntityKey();
			query.append(getAddAttributeQuery(tableName, columnName, dataType, reverseQueryList,
					isAddAssociationQuery));
		}
		else
		{
			tableName = targetEntity.getTableProperties().getName();
			String columnName = constraintProperties.getTargetEntityKey();
			query.append(getAddAttributeQuery(tableName, columnName, dataType, reverseQueryList,
					isAddAssociationQuery));

		}

		logDebug("getQueryPartForAssociation", "exiting method");
		return query.toString();
	}

	private String getAddAttributeQuery(String tableName, String columnName, String dataType,
			List reverseQueryList, boolean isAddAssociationQuery)
	{
		StringBuffer query = new StringBuffer();
		query.append(ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ADD_KEYWORD + WHITESPACE
				+ OPENING_BRACKET + WHITESPACE);
		query.append(columnName + WHITESPACE + dataType + WHITESPACE + CLOSING_BRACKET);
		String rollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + DROP_KEYWORD
				+ WHITESPACE + COLUMN_KEYWORD + WHITESPACE + columnName;

		if (isAddAssociationQuery)
		{
			reverseQueryList.add(rollbackQuery);
			return query.toString();
		}
		else
		{
			reverseQueryList.add(query.toString());
			return rollbackQuery;
		}
	}

	/**
	 * This method builds the query part for the primitive attribute 
	 * @param attribute primitive attribute for which to build the query.
	 * @return String query part of the primitive attribute.
	 * @throws DataTypeFactoryInitializationException 
	 */
	private String getQueryPartForAttribute(Attribute attribute, boolean processConstraints)
			throws DynamicExtensionsSystemException
	{

		String attributeQuery = null;
		if (attribute != null)
		{
			String columnName = attribute.getColumnProperties().getName();
			String isUnique = "";
			String nullConstraint = "";
			String defaultConstraint = "";
			if (processConstraints)
			{
				if (attribute.getIsPrimaryKey())
				{
					isUnique = CONSTRAINT_KEYWORD + WHITESPACE
							+ attribute.getColumnProperties().getName() + UNDERSCORE
							+ UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE + UNIQUE_KEYWORD;
				}
				nullConstraint = "NULL";

				if (!attribute.getIsNullable())
				{
					nullConstraint = "NOT NULL";
				}

				if (attribute.getAttributeTypeInformation().getDefaultValue() != null)
				{
					defaultConstraint = DEFAULT_KEYWORD
							+ WHITESPACE
							+ getFormattedValue(attribute, attribute.getAttributeTypeInformation()
									.getDefaultValue().getValueAsObject());
				}

			}

			attributeQuery = columnName + " " + getDatabaseTypeAndSize(attribute) + WHITESPACE
					+ defaultConstraint + WHITESPACE + isUnique + WHITESPACE + nullConstraint;
		}
		return attributeQuery;
	}

	/**
	 * This method returns the database type and size of the attribute passed to it which becomes the part of the query for that attribute.
	 * @param attribute Attribute object for which to get the database type and size.
	 * @return String that specifies the data base type and size.
	 * @throws DynamicExtensionsSystemException 
	 * @throws DataTypeFactoryInitializationException 
	 */
	private String getDatabaseTypeAndSize(Attribute attribute)
			throws DynamicExtensionsSystemException

	{
		try
		{
			DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();
			AttributeTypeInformationInterface attributeInformation = attribute
					.getAttributeTypeInformation();
			if (attributeInformation instanceof StringAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("String");
			}
			else if (attributeInformation instanceof IntegerAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Integer");
			}
			else if (attributeInformation instanceof DateAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Date");
			}
			else if (attributeInformation instanceof FloatAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Float");
			}
			else if (attributeInformation instanceof BooleanAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Boolean");
			}
			else if (attributeInformation instanceof DoubleAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Double");
			}
			else if (attributeInformation instanceof LongAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Long");
			}
			else if (attributeInformation instanceof ShortAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Short");
			}

		}
		catch (DataTypeFactoryInitializationException e)
		{
			throw new DynamicExtensionsSystemException("Could Not get data type attribute", e);
		}

		return null;
	}

	/**
	 * This method returns the dabase type for idenitifier.
	 * @return String database type for the identifier.
	 * @throws DynamicExtensionsSystemException exception is thrown if factory is not instanciated properly.
	 */
	private String getDataTypeForIdentifier() throws DynamicExtensionsSystemException
	{
		DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();
		return dataTypeFactory.getDatabaseDataType("Integer");
	}

	/**
	 * Returns a collection of entities having attribute with the given name  
	 * @param attributeName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByAttributeName(String attributeName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getAllContainers()
	 */
	public Collection<ContainerInterface> getAllContainers()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return getAllObjects(ContainerInterface.class.getName());
	}

	/**
	 * This method returns object for a given class name and identifer 
	 * @param objectName  name of the class of the object
	 * @param identifier identifier of the object
	 * @return  obejct
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private AbstractMetadataInterface getObjectByIdentifier(String objectName, String identifier)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		AbstractMetadataInterface object;
		try
		{
			List objectList = bizLogic.retrieve(objectName, Constants.ID, identifier);

			if (objectList == null || objectList.size() == 0)
			{
				throw new DynamicExtensionsApplicationException("OBJECT_NOT_FOUND");
			}

			object = (EntityInterface) objectList.get(0);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return object;
	}

	/**
	 *  Returns all entities in the whole system for a given type of the object
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Collection getAllObjects(String objectName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		Collection objectList = new HashSet();

		try
		{
			objectList = bizLogic.retrieve(objectName);
			if (objectList == null)
			{
				objectList = new HashSet();
			}
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return objectList;
	}

	/**
	 * Returns a collection of entity objects given the entity description
	 * @param entityDescription
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntityByDescription(String entityDescription)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of Entity objects given the attribute description
	 * @param attributeDescription
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByAttributeDescription(String attributeDescription)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of entity objects given the entity concept name.
	 * @param entityConceptName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByConceptName(String entityConceptName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of entities given attribute concept code.
	 * @param attributeConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByAttributeConceptCode(String attributeConceptCode)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of entities given the attribute concept name
	 * @param attributeConceptname
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByAttributeConceptName(String attributeConceptName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of entity objects given the entity object with specific criteria. 
	 * @param entityInterface
	 * @return
	 */
	public Collection findEntity(EntityInterface entityInterface)
	{
		return null;
	}

	/**
	 * This method is used to save the container into the database.
	 * @param containerInterface container to save
	 * @return ContainerInterface container Interface that is saved.
	 * @throws DynamicExtensionsSystemException Thrown if for any reason operation can not be completed.
	 * @throws DynamicExtensionsApplicationException Thrown if the entity name already exists.
	 * @throws DynamicExtensionsSystemException 
	 */
	public ContainerInterface persistContainer(ContainerInterface containerInterface)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Container container = (Container) containerInterface;
		Stack rollbackQueryStack = new Stack();
		if (container == null)
		{
			throw new DynamicExtensionsSystemException("Container passed is null");
		}

		Entity entity = (Entity) container.getEntity();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		boolean isentitySaved = true;
		if (entity != null && entity.getId() == null)
		{
			isentitySaved = false;
		}
		boolean isContainerSaved = true;
		if (container.getId() == null)
		{
			isContainerSaved = false;
		}
		try
		{
			hibernateDAO.openSession(null);
			if (entity != null)
			{
				saveOrUpdateEntity(entity, hibernateDAO, rollbackQueryStack, isentitySaved);
			}
			preSaveProcessContainer(container);
			if (isContainerSaved)
			{
				hibernateDAO.update(container, null, false, false, false);
			}
			else
			{
				hibernateDAO.insert(container, null, false, false);
			}
			hibernateDAO.commit();

		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, entity, e);
			throw new DynamicExtensionsSystemException(
					"Exception occured while opening a session to save the container.");
		}
		catch (UserNotAuthorizedException e)
		{
			// TODO Auto-generated catch block
			rollbackQueries(rollbackQueryStack, entity, e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				rollbackQueries(rollbackQueryStack, entity, e);
			}
		}

		//hibernateDAO.
		return container;
	}

	/**
	 * @param container container
	 */
	private void preSaveProcessContainer(Container container)
	{
		if (container.getEntity() != null)
		{
			preSaveProcessEntity(container.getEntity());
		}
	}

	/**
	 * @param entity entity
	 */
	private void preSaveProcessEntity(EntityInterface entity)
	{
		if (entity.getId() != null)
		{
			entity.setLastUpdated(new Date());
		}
		else
		{
			entity.setCreatedDate(new Date());
			entity.setLastUpdated(entity.getCreatedDate());
		}
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#insertData(edu.common.dynamicextensions.domaininterface.EntityInterface, java.util.Map)
	 */
	public Long insertData(EntityInterface entity, Map dataValue)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		if (entity == null || dataValue == null || dataValue.isEmpty())
		{
			throw new DynamicExtensionsSystemException("Input to insert data is null");
		}

		StringBuffer columnNameString = new StringBuffer("IDENTIFIER ");
		Long identifier = getNextIdentifier(entity);
		StringBuffer columnValuesString = new StringBuffer(identifier.toString());
		String tableName = entity.getTableProperties().getName();

		List<CollectionAttributeRecord> collectionRecords = new ArrayList<CollectionAttributeRecord>();
		
		
		Set uiColumnSet = dataValue.keySet();
		Iterator uiColumnSetIter = uiColumnSet.iterator();

		while (uiColumnSetIter.hasNext())
		{
			AbstractAttribute attribute = (AbstractAttribute) uiColumnSetIter.next();
			if (attribute instanceof AttributeInterface)
			{
				AttributeInterface primitiveAttribute = (AttributeInterface) attribute ;
				Object value = dataValue.get(primitiveAttribute);
				
				if(primitiveAttribute.getIsCollection()) {
					CollectionAttributeRecord collectionRecord = populateCollectionAttributeRecord(null,entity,primitiveAttribute,identifier,(List<String>) value);
					collectionRecords.add(collectionRecord);
				} else {
					columnNameString.append(" , ");
					columnValuesString.append(" , ");
					String dbColumnName = primitiveAttribute.getColumnProperties()
							.getName();

					columnNameString.append(dbColumnName);
					value = getFormattedValue(attribute, value);
					columnValuesString.append(value);
				}
			}
			else
			{
				//TODO Process associations here.
			}
		}

		StringBuffer query = new StringBuffer("INSERT INTO " + tableName + " ( ");
		query.append(columnNameString);
		query.append(" ) VALUES (");
		query.append(columnValuesString);
		query.append(" ) ");

		HibernateDAO hibernateDAO = null;
		try
		{   
			logDebug("insertData", "Query is: " + query.toString());

			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);

			hibernateDAO.openSession(null);
		    Connection conn = DBUtil.getConnection();
			PreparedStatement statement = conn.prepareStatement(query.toString());
			statement.executeUpdate();
			
			for(CollectionAttributeRecord collectionAttributeRecord : collectionRecords) {
				//logDebug("insertData", "Inserting multi select: " +  collectionAttributeRecord.getValue());
				hibernateDAO.insert(collectionAttributeRecord, null, false, false);
			}
			
			hibernateDAO.commit();
		}
		catch (Exception e)
		{
			try
			{
				hibernateDAO.rollback();
			}
			catch (DAOException e1)
			{
				throw new DynamicExtensionsSystemException("Error while inserting data", e1);
			}
			throw new DynamicExtensionsSystemException("Error while inserting data", e);
		}
		finally {
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while inserting data", e);
			}
			
		}

		return identifier;
	}
	
	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#editData(edu.common.dynamicextensions.domaininterface.EntityInterface, java.util.Map, java.lang.Long)
	 */
	public boolean editData(EntityInterface entity, Map dataValue,Long recordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		if (entity == null || dataValue == null || dataValue.isEmpty())
		{
			throw new DynamicExtensionsSystemException("Input to edit data is null");
		}

		StringBuffer updateColumnString = new StringBuffer();
		String tableName = entity.getTableProperties().getName();

		List<CollectionAttributeRecord> collectionRecords = new ArrayList<CollectionAttributeRecord>();
		List<CollectionAttributeRecord> deleteCollectionRecords = new ArrayList<CollectionAttributeRecord>();
		
		Set uiColumnSet = dataValue.keySet();
		Iterator uiColumnSetIter = uiColumnSet.iterator();

		while (uiColumnSetIter.hasNext())
		{
			AbstractAttribute attribute = (AbstractAttribute) uiColumnSetIter.next();
			if (attribute instanceof AttributeInterface)
			{
				AttributeInterface primitiveAttribute = (AttributeInterface) attribute ;
				Object value = dataValue.get(primitiveAttribute);
				
				if(primitiveAttribute.getIsCollection()) {
					  CollectionAttributeRecord collectionRecord = getCollectionAttributeRecord(entity.getId(),primitiveAttribute.getId(), recordId);
					  List<String> listOfValues = (List<String>) value;
					  
					  if (!listOfValues.isEmpty() ) {
						  collectionRecord = populateCollectionAttributeRecord(collectionRecord,entity,primitiveAttribute,recordId,(List<String>) value);
						  collectionRecords.add(collectionRecord);
					  }
					  
					  if (collectionRecord != null && listOfValues.isEmpty()) {
						  deleteCollectionRecords.add(collectionRecord);
					  }
					
				} else {
					String dbColumnName = primitiveAttribute.getColumnProperties()
					.getName();
					
					if(updateColumnString.length() != 0 ) {
						updateColumnString.append(WHITESPACE + COMMA + WHITESPACE);
					}

					updateColumnString.append(dbColumnName);
					updateColumnString.append(WHITESPACE + EQUAL + WHITESPACE);
					value = getFormattedValue(attribute, value);
					updateColumnString.append(value);
				}
			}
		}

		StringBuffer query = null;
		if (updateColumnString.length() != 0) {
			query = new StringBuffer("UPDATE " + tableName + " SET ");
			query.append(updateColumnString);
			query.append(" where ");
			query.append(IDENTIFIER);
			query.append(WHITESPACE + EQUAL + WHITESPACE);
			query.append(recordId);
			
		}
		HibernateDAO hibernateDAO = null;
		try
		{   

			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);

			hibernateDAO.openSession(null);
		    
			if(updateColumnString.length() != 0) {
				logDebug("editData", "Query is: " + query.toString());
				Connection conn = DBUtil.getConnection();
				PreparedStatement statement = conn.prepareStatement(query.toString());
				statement.executeUpdate();
		    }
			
			for(CollectionAttributeRecord collectionAttributeRecord : collectionRecords) {
				logDebug("editData", "updating multi select: " +  collectionAttributeRecord.getValueCollection());
				hibernateDAO.update(collectionAttributeRecord, null, false, false,false);
			}
			
			for(CollectionAttributeRecord collectionAttributeRecord : deleteCollectionRecords) {
				logDebug("editData", "deleting multi select: " +  collectionAttributeRecord.getValueCollection());
				hibernateDAO.update(collectionAttributeRecord, null, false, false,false);
			}
			
			hibernateDAO.commit();
		}
		catch (Exception e)
		{
			try
			{
				hibernateDAO.rollback();
			}
			catch (DAOException e1)
			{
				throw new DynamicExtensionsSystemException("Error while editing data", e1);
			}
			throw new DynamicExtensionsSystemException("Error while editing data", e);
		}
		finally {
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while editing data", e);
			}
			
		}

		return true;
	}

	/**
	 * This method returns a list of <CollectionAttributeRecord> that for a particular multiselect attribute of 
	 * the entity.
	 * @param collectionRecord 
	 * 
	 * @param entity entity for which data has been entered.
	 * @param primitiveAttribute attribute for which data has been entered.
	 * @param identifier id of the record
	 * @param values List of values for this multiselect attribute
	 * @return  list of <CollectionAttributeRecord>
	 */
	private CollectionAttributeRecord populateCollectionAttributeRecord(CollectionAttributeRecord collectionRecord, EntityInterface entity, AttributeInterface primitiveAttribute, Long identifier, List<String> values)
	{
		if (collectionRecord == null) {
			collectionRecord = new CollectionAttributeRecord();
			collectionRecord.setValueCollection(new HashSet<CollectionAttributeRecordValue>());
		} else {
			collectionRecord.getValueCollection().clear();
		}
		Collection<CollectionAttributeRecordValue> valueCollection = collectionRecord.getValueCollection();
		
		
		collectionRecord.setEntity(entity);
		collectionRecord.setAttribute(primitiveAttribute);
		collectionRecord.setRecordId(identifier);
		for(String value :values) {
			CollectionAttributeRecordValue collectionAttributeRecordValue = new CollectionAttributeRecordValue();
			collectionAttributeRecordValue.setValue(value);
			valueCollection.add(collectionAttributeRecordValue);
		}
		
		return collectionRecord;
	}

	/**
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 */
	private String getFormattedValue(AbstractAttribute attribute, Object value)
	{
		String formattedvalue = null;
		AttributeTypeInformationInterface attributeInformation = ((Attribute) attribute)
				.getAttributeTypeInformation();
		if (attribute == null)
		{
			formattedvalue = null;
		}

		else if (attributeInformation instanceof StringAttributeTypeInformation)
		{
			formattedvalue = "'" + value + "'";
		}
		else if (attributeInformation instanceof DateAttributeTypeInformation)
		{
            String dateValue = Utility.parseDateToString(((Date) value), Variables.datePattern);
			formattedvalue = Variables.strTodateFunction + "('" + dateValue + "','"
					+ Variables.datePattern + "')";
		}
		else
		{
			formattedvalue = value.toString();
		}
        logDebug("getFormattedValue","The formatted value for attribute " + attribute.getName() + "is " + formattedvalue);
		return formattedvalue;

	}

	/**
	 * This method returns mapping for all attributes of given entity to its actual database column name.
	 *  
	 * @param entity entity which is to  be processed.
	 * @return map 
	 * 
	 * key      attribute name -- name displayed on the UI for the attribute
	 * value    column name    -- actual database column name for the attriubute.
	 */
	//    private Map getDbColumnNameMap(EntityInterface entity)
	//    {
	//        Map colNameMap = new HashMap();
	//        Iterator attribbuteIterator = entity.getAttributeCollection()
	//                .iterator();
	//        while (attribbuteIterator.hasNext())
	//        {
	//            Attribute attribute = (Attribute) attribbuteIterator.next();
	//            colNameMap.put(attribute.getName(), attribute.getColumnProperties()
	//                    .getName());
	//        }
	//
	//        return colNameMap;
	//    }
	/**
	 * Method generates the next identifier for the table that stores the value of the passes entity.
	 * @param entity
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	synchronized private Long getNextIdentifier(EntityInterface entity)
			throws DynamicExtensionsSystemException
	{

		String entityTableName = entity.getTableProperties().getName();
		StringBuffer queryToGetNextIdentifier = new StringBuffer("SELECT MAX(IDENTIFIER) FROM "
				+ entityTableName);
		List resultList = null;
		try
		{
			resultList = getResultInList(queryToGetNextIdentifier.toString(),
					new SessionDataBean(), false, false, null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}

		if (resultList == null)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
		List internalList = (List) resultList.get(0);
		if (internalList == null || internalList.isEmpty())
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
		String idString = (String) (internalList.get(0));

		Long identifier = null;

		if (idString == null || idString.trim().equals(""))
		{
			identifier = new Long(0);
		}
		else
		{
			identifier = new Long(idString);
		}

		long id = identifier.longValue();
		id++;
		identifier = new Long(id);
		return identifier;
	}

	/**
	 * Executes a query and return result set.
	 * @param queryToGetNextIdentifier
	 * @param sessionDataBean
	 * @param isSecureExecute
	 * @param hasConditionOnIdentifiedField
	 * @param queryResultObjectDataMap
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private List getResultInList(String queryToGetNextIdentifier, SessionDataBean sessionDataBean,
			boolean isSecureExecute, boolean hasConditionOnIdentifiedField,
			Map queryResultObjectDataMap) throws DAOException, ClassNotFoundException
	{
		List resultList = null;
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDAO.openSession(null);
			resultList = jdbcDAO.executeQuery(queryToGetNextIdentifier, sessionDataBean,
					isSecureExecute, hasConditionOnIdentifiedField, queryResultObjectDataMap);
		}
		catch (DAOException daoException)
		{
			daoException.printStackTrace();
			throw new DAOException("Exception while retrieving the query result", daoException);
		}
		finally
		{
			try
			{
				jdbcDAO.closeSession();
			}
			catch (DAOException daoException)
			{
				throw new DAOException("Exception while closing the jdbc session", daoException);
			}
		}
		return resultList;
	}

	/**
	 * This method is used by create as well as edit entity methods. This method holds all the common part 
	 * related to saving the entity into the database and also handling the exceptions .
	 * @param entityInterface Entity to be stored in the database.
	 * @param isNew flag for whether it is a save or update.
	 * @param hibernateDAO 
	 * @param isNewFlag 
	 * @return Entity . Stored instance of the entity.
	 * @throws DynamicExtensionsApplicationException System exception in case of any fatal errors.
	 * @throws DynamicExtensionsSystemException Thrown in case of duplicate name or authentication failure.
	 */
	private EntityInterface saveOrUpdateEntity(EntityInterface entityInterface,
			HibernateDAO hibernateDAO, Stack rollbackQueryStack, boolean isEntitySaved)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		logDebug("saveOrUpdateEntity", "Entering method");

		Entity entity = (Entity) entityInterface;
		List reverseQueryList = new LinkedList();
		List queryList = null;

		checkForDuplicateEntityName(entity);
		Entity databaseCopy = null;
		try
		{
			if (!isEntitySaved)
			{
				preSaveProcessEntity(entity);
				hibernateDAO.insert(entity, null, false, false);
			}
			else
			{
				databaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, entity.getId());
				hibernateDAO.update(entity, null, false, false, false);

			}
			postSaveProcessEntity(entity, hibernateDAO, rollbackQueryStack);
			hibernateDAO.update(entity, null, false, false, false);

			if (!isEntitySaved)
			{
				queryList = getCreateEntityQueryList(entity, reverseQueryList, hibernateDAO,
						rollbackQueryStack);
			}
			else
			{
				queryList = getUpdateEntityQueryList(entity, (Entity) databaseCopy,
						reverseQueryList);
			}

			executeQueries(queryList, reverseQueryList, rollbackQueryStack);

		}
		catch (UserNotAuthorizedException e)
		{
			logDebug("saveOrUpdateEntity", DynamicExtensionsUtility.getStackTrace(e));
			throw new DynamicExtensionsApplicationException(
					"User is not authorised to perform this action", e, DYEXTN_A_002);
		}
		catch (Exception e)
		{
			logDebug("saveOrUpdateEntity", DynamicExtensionsUtility.getStackTrace(e));
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}

		logDebug("saveOrUpdateEntity", "Exiting Method");
		return entity;//(Entity) getEntityByIdentifier(entity.getId().toString());
	}

	/**
	 * This method is used to execute the data table queries for entity in case of editing the entity.
	 * This method takes each attribute of the entity and then scans for any changes and builds the alter query
	 * for each attribute for the entity.
	 * @param entity Entity for which to generate and execute the alter queries.
	 * @param databaseCopy Old database copy of the entity.
	 * @return Stack Stack holding the rollback queries in case of any exception
	 * @throws DynamicExtensionsSystemException System exception in case of any fatal error
	 * @throws DynamicExtensionsApplicationException Thrown in case of authentication failure or duplicate name.
	 */
	private List getUpdateEntityQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		logDebug("getUpdateEntityQueryList", "Entering method");

		List updateAttributeQueryList = getUpdateAttributeQueryList(entity, databaseCopy,
				attributeRollbackQueryList);
		List updateassociationsQueryList = getUpdateAssociationsQueryList(entity, databaseCopy,
				attributeRollbackQueryList);

		List updateQueryList = new ArrayList();

		updateQueryList.addAll(updateAttributeQueryList);
		updateQueryList.addAll(updateassociationsQueryList);

		logDebug("getUpdateEntityQueryList", "Exiting method");
		return updateQueryList;
	}

	/**
	 * @param entity
	 * @param databaseCopy
	 * @param attributeRollbackQueryList
	 * @return
	 */
	private List getUpdateAssociationsQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException
	{
		logDebug("getUpdateAssociationsQueryList", "Entering method");
		List associationsQueryList = new ArrayList();
		boolean isAddAssociationQuery = true;

		Collection associationCollection = entity.getAssociationCollection();

		if (associationCollection != null && !associationCollection.isEmpty())
		{
			Iterator associationIterator = associationCollection.iterator();

			while (associationIterator.hasNext())
			{
				Association association = (Association) associationIterator.next();
				Association associationDatabaseCopy = (Association) databaseCopy
						.getAttributeByIdentifier(association.getId());

				if (associationDatabaseCopy == null)
				{
					isAddAssociationQuery = true;
					String newAssociationQuery = getQueryPartForAssociation(association,
							attributeRollbackQueryList, isAddAssociationQuery);
					associationsQueryList.add(newAssociationQuery);
				}
				else
				{
					if (isCardinalityChanged(association, associationDatabaseCopy))
					{
						isAddAssociationQuery = false;
						String savedAssociationRemoveQuery = getQueryPartForAssociation(
								associationDatabaseCopy, attributeRollbackQueryList,
								isAddAssociationQuery);
						associationsQueryList.add(savedAssociationRemoveQuery);

						isAddAssociationQuery = true;
						String newAssociationAddQuery = getQueryPartForAssociation(association,
								attributeRollbackQueryList, isAddAssociationQuery);
						associationsQueryList.add(newAssociationAddQuery);
					}
				}
			}
		}
		processRemovedAssociation(entity, databaseCopy, associationsQueryList,
				attributeRollbackQueryList);

		logDebug("getUpdateAssociationsQueryList", "Exiting method");
		return associationsQueryList;
	}

	/**
	 * @param association
	 * @param associationDatabaseCopy
	 * @return
	 */
	private boolean isCardinalityChanged(Association association,
			Association associationDatabaseCopy)
	{
		Cardinality sourceMaxCardinality = association.getSourceRole().getMaximumCardinality();
		Cardinality targetMaxCardinality = association.getTargetRole().getMaximumCardinality();

		Cardinality sourceMaxCardinalityDatabaseCopy = associationDatabaseCopy.getSourceRole()
				.getMaximumCardinality();
		Cardinality targetMaxCardinalityDatabaseCopy = associationDatabaseCopy.getTargetRole()
				.getMaximumCardinality();

		if (!sourceMaxCardinality.equals(sourceMaxCardinalityDatabaseCopy)
				|| !targetMaxCardinality.equals(targetMaxCardinalityDatabaseCopy))
		{
			return true;
		}
		return false;
	}

	/**
	 * This method processes any associations that are deleted from the entity.
	 * @param entity
	 * @param databaseCopy
	 * @param associationsQueryList
	 * @param attributeRollbackQueryList
	 * @throws DynamicExtensionsSystemException 
	 */
	private void processRemovedAssociation(Entity entity, Entity databaseCopy,
			List associationsQueryList, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException
	{
		logDebug("processRemovedAssociation", "Entering method");

		Collection savedAssociationCollection = databaseCopy.getAssociationCollection();
		String tableName = entity.getTableProperties().getName();

		if (savedAssociationCollection != null && !savedAssociationCollection.isEmpty())
		{
			Iterator savedAssociationIterator = savedAssociationCollection.iterator();
			while (savedAssociationIterator.hasNext())
			{
				Association savedAssociation = (Association) savedAssociationIterator.next();
				Association association = (Association) entity
						.getAttributeByIdentifier(savedAssociation.getId());;

				// removed ??
				if (association == null)
				{
					boolean isAddAssociationQuery = false;
					String removeAssociationQuery = getQueryPartForAssociation(savedAssociation,
							attributeRollbackQueryList, isAddAssociationQuery);
					associationsQueryList.add(removeAssociationQuery);
				}
			}
		}
		logDebug("processRemovedAssociation", "Exiting method");
	}

	private List getUpdateAttributeQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		logDebug("getUpdateAttributeQueryList", "Entering method");
		Collection attributeCollection = entity.getAttributeCollection();
		List attributeQueryList = new ArrayList();

		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();

			while (attributeIterator.hasNext())
			{
				Attribute attribute = (Attribute) attributeIterator.next();
				Attribute savedAttribute = (Attribute) databaseCopy
						.getAttributeByIdentifier(attribute.getId());

				if (savedAttribute == null || (!attribute.getIsCollection() && savedAttribute.getIsCollection()))
				{
					String attributeQuery = processAddAttribute(attribute,
							attributeRollbackQueryList);
					attributeQueryList.add(attributeQuery);
				}
				else
				{
					List modifiedAttributeQueryList = processModifyAttribute(attribute,
							savedAttribute, attributeRollbackQueryList);
					attributeQueryList.addAll(modifiedAttributeQueryList);
				}

			}

		}
		processRemovedAttributes(entity, databaseCopy, attributeQueryList,
				attributeRollbackQueryList);
		logDebug("getUpdateAttributeQueryList", "Exiting method");
		return attributeQueryList;
	}

	/**
	 * This method takes the edited attribtue and its database copy and then looks for any change
	 * Changes that are tracked in terms of data table query are 
	 * Change in the constraint NOT NULL AND UNIQUE
	 * <BR> Change in the database type of the column.
	 * @param attribute edited Attribute 
	 * @param savedAttribute original database copy of the edited attribute.
	 * @param attributeRollbackQueryList This list is updated with the roll back queries for the actual queries.
	 * @return List list of strings which hold the queries for the changed attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private List processModifyAttribute(Attribute attribute, Attribute savedAttribute,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List modifyAttributeQueryList = new ArrayList();
		String tableName = attribute.getEntity().getTableProperties().getName();
		String columnName = attribute.getColumnProperties().getName();
		boolean attributemodifiedFlag = false;
		/*        if (isAttributeChanged(attribute, savedAttribute))
		 {
		 */
		String newTypeClass = attribute.getAttributeTypeInformation().getClass().getName();
		String oldTypeClass = savedAttribute.getAttributeTypeInformation().getClass().getName();

		if (!newTypeClass.equals(oldTypeClass))
		{
			attributemodifiedFlag = true;
		}

		String modifyAttributeQuery = getQueryPartForAbstractAttribute(attribute, false);
		modifyAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + MODIFY_KEYWORD
				+ WHITESPACE + modifyAttributeQuery;

		String modifyAttributeRollbackQuery = getQueryPartForAbstractAttribute(savedAttribute,
				false);
		modifyAttributeRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
				+ MODIFY_KEYWORD + WHITESPACE + modifyAttributeRollbackQuery;
		//process nullable
		if (attribute.getIsNullable() && !savedAttribute.getIsNullable())
		{
			attributemodifiedFlag = true;
			modifyAttributeQuery = modifyAttributeQuery + WHITESPACE + NULL_KEYWORD;
			modifyAttributeRollbackQuery = modifyAttributeRollbackQuery + WHITESPACE + NOT_KEYWORD
					+ WHITESPACE + NULL_KEYWORD;
		}
		else if (!attribute.getIsNullable() && savedAttribute.getIsNullable())
		{
			attributemodifiedFlag = true;
			modifyAttributeQuery = modifyAttributeQuery + WHITESPACE + NOT_KEYWORD + WHITESPACE
					+ NULL_KEYWORD;
			//TODO add default constraint
			modifyAttributeRollbackQuery = modifyAttributeRollbackQuery + WHITESPACE + NULL_KEYWORD;
		}

		if (attributemodifiedFlag)
		{
			modifyAttributeQueryList.add(modifyAttributeQuery);
			attributeRollbackQueryList.add(modifyAttributeRollbackQuery);
		}

		/*        }
		 */
		if (attribute.getIsPrimaryKey() && !savedAttribute.getIsPrimaryKey())
		{

			String uniqueConstraintQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ ADD_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE + columnName
					+ UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE + UNIQUE_KEYWORD
					+ WHITESPACE + OPENING_BRACKET + columnName + CLOSING_BRACKET;
			String uniqueConstraintRollbackQuery = ALTER_TABLE + WHITESPACE + tableName
					+ WHITESPACE + DROP_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE
					+ columnName + UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX;

			modifyAttributeQueryList.add(uniqueConstraintQuery);
			attributeRollbackQueryList.add(uniqueConstraintRollbackQuery);

		}
		else if (!attribute.getIsPrimaryKey() && savedAttribute.getIsPrimaryKey())
		{
			String uniqueConstraintQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ DROP_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE + columnName
					+ UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX;
			String uniqueConstraintRollbackQuery = ALTER_TABLE + WHITESPACE + tableName
					+ WHITESPACE + ADD_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE
					+ columnName + UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE
					+ UNIQUE_KEYWORD + WHITESPACE + OPENING_BRACKET + columnName + CLOSING_BRACKET;

			modifyAttributeQueryList.add(uniqueConstraintQuery);
			attributeRollbackQueryList.add(uniqueConstraintRollbackQuery);
		}
		return modifyAttributeQueryList;
	}

	/**This method returns true if the new attribute is actually changed with regards to its database copy.
	 * Otherwise it returns false.
	 * @param abstractAttribute
	 * @param abstractSavedAttribute
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	private boolean isAttributeChanged(Attribute attribute, Attribute savedAttribute)
			throws DynamicExtensionsSystemException
	{

		if (!getDatabaseTypeAndSize(attribute).equals(getDatabaseTypeAndSize(savedAttribute)))
		{
			return true;
		}

		return false;
	}

	/**
	 * This method builds the query part for the newly added attribute.
	 * @param attribute Newly added attribute in the entity.
	 * @param attributeRollbackQueryList This list is updated with the rollback queries for the actual queries.
	 * @return Srting The actual query part for the new attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private String processAddAttribute(Attribute attribute, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		String columnName = attribute.getColumnProperties().getName();
		String tableName = attribute.getEntity().getTableProperties().getName();

		String newAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ADD_KEYWORD
				+ WHITESPACE + getQueryPartForAbstractAttribute(attribute, true);

		String newAttributeRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
				+ DROP_KEYWORD + WHITESPACE + COLUMN_KEYWORD + WHITESPACE + columnName;

		attributeRollbackQueryList.add(newAttributeRollbackQuery);

		return newAttributeQuery;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getRecordById(edu.common.dynamicextensions.domaininterface.EntityInterface, java.lang.Long)
	 */
	public Map getRecordById(EntityInterface entity, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Map recordValues = new HashMap();

		if (entity == null || entity.getId() == null || recordId == null)
		{
			throw new DynamicExtensionsSystemException("Invalid Input");
		}

		Collection attributesCollection = entity.getAttributeCollection();
		List<AttributeInterface> collectionAtributes = new ArrayList<AttributeInterface>();

		String tableName = entity.getTableProperties().getName();
		List<String>  selectColumnNameList = new ArrayList<String> ();
		String[] whereColumnName = new String[]{IDENTIFIER};
		String[] whereColumnCondition = new String[]{"="};
		Object[] whereColumnValue = new Object[]{recordId};

		Iterator attriIterator = attributesCollection.iterator();
		Map columnNameMap = new HashMap();
		int index = 0;
		while (attriIterator.hasNext())
		{
			AttributeInterface attribute = (AttributeInterface) attriIterator.next();
			
			if (attribute.getIsCollection()) {
				collectionAtributes.add(attribute);
			} else {
				String dbColumnName = attribute.getColumnProperties().getName();
				String uiColumnName = attribute.getName();
				selectColumnNameList.add(dbColumnName);
				//selectColumnName[index] = dbColumnName;
				columnNameMap.put(dbColumnName, uiColumnName);
				index++;
			}
		}

		String[] selectColumnName = new String[selectColumnNameList.size()];
		for(int i = 0; i < selectColumnNameList.size() ;i++) {
			selectColumnName[i] = selectColumnNameList.get(i);
		}
		
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao  = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			
			List result = jdbcDao.retrieve(tableName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, null);
			List innerList = null;

			if (result != null && result.size() != 0)
			{
				innerList = (List) result.get(0);
			}
			if (innerList != null)
			{
				for (int i = 0; i < innerList.size(); i++)
				{
					String value = (String) innerList.get(i);
					String dbColumnName = selectColumnName[i];
					String uiColumnName = (String) columnNameMap.get(dbColumnName);
					recordValues.put(uiColumnName, value);
				}
			}

			for(AttributeInterface attribute: collectionAtributes) {
				List<String> valueList = getCollectionAttributeRecordValues(entity.getId(),attribute.getId(),recordId);
				recordValues.put(attribute.getName(), valueList);
			}

		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while retrieving the data", e);
		} finally {
			
			try
			{
				jdbcDao.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while retrieving the data", e);
			}
		}
		return recordValues;
	}

	/**
	 * This method processes all the attributes that previoulsy saved but removed by editing.
	 * @param entity
	 * @param databaseCopy
	 * @param attributeQueryList
	 * @param attributeRollbackQueryList
	 * @throws DynamicExtensionsSystemException 
	 */
	private void processRemovedAttributes(Entity entity, Entity databaseCopy,
			List attributeQueryList, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException
	{
		Collection savedAttributeCollection = databaseCopy.getAbstractAttributeCollection();
		String tableName = entity.getTableProperties().getName();

		if (savedAttributeCollection != null && !savedAttributeCollection.isEmpty())
		{
			Iterator savedAttributeIterator = savedAttributeCollection.iterator();
			while (savedAttributeIterator.hasNext())
			{
				AbstractAttribute savedAbstractAttribute = (AbstractAttribute) savedAttributeIterator
						.next();

				if (savedAbstractAttribute instanceof Attribute)
				{
					Attribute savedAttribute = (Attribute) savedAbstractAttribute;
					Attribute attribute = (Attribute) entity
							.getAttributeByIdentifier(savedAbstractAttribute.getId());;
					// removed ??
					if (attribute == null || (attribute.getIsCollection() && !savedAttribute.getIsCollection()))
					{
						String columnName = savedAttribute.getColumnProperties().getName();

						String removeAttributeQuery = ALTER_TABLE + WHITESPACE + tableName
								+ WHITESPACE + DROP_KEYWORD + WHITESPACE + COLUMN_KEYWORD
								+ WHITESPACE + columnName;

						String removeAttributeQueryRollBackQuery = ALTER_TABLE + WHITESPACE
								+ tableName + WHITESPACE + ADD_KEYWORD + WHITESPACE
								+ getQueryPartForAbstractAttribute(attribute, true);

						attributeQueryList.add(removeAttributeQuery);
						attributeRollbackQueryList.add(removeAttributeQueryRollBackQuery);
					}

				}

			}

		}
	}

	/**
	 * @param entity entity
	 */
	private void preSaveProcessEntityGroup(EntityGroupInterface entityGroup)
	{
		if (entityGroup.getId() != null)
		{
			entityGroup.setLastUpdated(new Date());
		}
		else
		{
			entityGroup.setCreatedDate(new Date());
			entityGroup.setLastUpdated(entityGroup.getCreatedDate());
		}
	}

	/**
	 * This method is used by create as well as edit entity group. This method holds all the common part 
	 * related to saving the entity group into the database and also handling the exceptions .
	 * @param entityGroupInterface EntityGroupInterface  to be stored in the database.
	 * @param isNew flag for whether it is a save or update.
	 * @return EntityGroup . Stored instance of the entity group.
	 * @throws DynamicExtensionsApplicationException System exception in case of any fatal errors.
	 * @throws DynamicExtensionsSystemException Thrown in case of duplicate name or authentication failure.
	 */
	private EntityGroup saveOrUpdateEntityGroup(EntityGroupInterface entityGroupInterface,
			boolean isNew) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		logDebug("saveOrUpdateEntityGroup", "Entering method");
		EntityGroup entityGroup = (EntityGroup) entityGroupInterface;
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		Stack stack = null;
		try
		{
			checkForDuplicateEntityGroupName(entityGroup);

			if (isNew)
			{
				hibernateDAO.openSession(null);
				hibernateDAO.insert(entityGroup, null, false, false);
			}
			else
			{
				Long id = entityGroup.getId();
				hibernateDAO.openSession(null);
				List entityGroupList = hibernateDAO.retrieve(EntityGroup.class.getName(),
						Constants.ID, id);
				EntityGroup databaseCopy = null;
				if (entityGroupList != null && !entityGroupList.isEmpty())
				{
					databaseCopy = (EntityGroup) entityGroupList.get(0);
				}
				hibernateDAO.closeSession();
				hibernateDAO.openSession(null);
				hibernateDAO.update(entityGroup, null, false, false, false);
				hibernateDAO.update(entityGroup, null, false, false, false);

			}
			hibernateDAO.commit();
		}
		catch (UserNotAuthorizedException e)
		{

			throw new DynamicExtensionsApplicationException(
					"User is not authorised to perform this action", e, DYEXTN_A_002);
		}
		catch (DAOException e)
		{
			try
			{
				hibernateDAO.rollback();

			}
			catch (Exception e1)
			{
				throw new DynamicExtensionsSystemException(
						"Exception occured while rolling back the session", e1, DYEXTN_S_001);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();

			}
			catch (DAOException e)
			{
				e.printStackTrace();
				throw new DynamicExtensionsSystemException(
						"Exception occured while closing the session", e, DYEXTN_S_001);
			}

		}
		logDebug("saveOrUpdateEntity", "Exiting Method");
		return entityGroup;
	}

	/**
	 * This method checks if the entity group can be created with the given name or not. 
	 * This method will check for the duplicate name as per the following rule
	 * @param entityGroup Entity Group whose name's uniqueness is to be checked.
	 * @throws DynamicExtensionsApplicationException This will basically act as a duplicate name exception.
	 */
	private void checkForDuplicateEntityGroupName(EntityGroup entityGroup)
			throws DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
	}

	/**
	 *  This method executes the HQL query given the query name and query parameters.
	 *  The queries are specified in the EntityManagerHQL.hbm.xml file.For each query a name is given.
	 *  Each query is replaced with parameters before execution.The parametrs are given by each calling method.
	 * @param entityConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Collection executeHQL(String queryName, Map substitutionParameterMap)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Collection entityCollection = new HashSet();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Exception occured while opening a session to save the container.");
		}
		try
		{
			Query query = substitutionParameterForQuery(queryName, substitutionParameterMap);
			entityCollection = query.list();
			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			try
			{
				hibernateDAO.rollback();

			}
			catch (DAOException e1)
			{
				throw new DynamicExtensionsSystemException("Error while rolling back the session",
						e1);
			}

		}

		catch (HibernateException e)
		{
			throw new DynamicExtensionsSystemException("Error while rolling back the session", e);
		}

		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while closing the session", e);
			}
		}
		return entityCollection;
	}

	/**
	 * This method substitues the parameters from substitutionParameterMap into the input query. 
	 * @param substitutionParameterMap
	 * @throws HibernateException 
	 */
	private Query substitutionParameterForQuery(String queryName, Map substitutionParameterMap)
			throws HibernateException
	{
		Session session = DBUtil.currentSession();
		Query q = session.getNamedQuery(queryName);
		for (int counter = 0; counter < substitutionParameterMap.size(); counter++)
		{
			HQLPlaceHolderObject hPlaceHolderObject = (HQLPlaceHolderObject) substitutionParameterMap
					.get(counter + "");
			String objectType = hPlaceHolderObject.getType();
			if (objectType.equals("string"))
			{
				q.setString(counter, hPlaceHolderObject.getValue() + "");
			}
			else if (objectType.equals("integer"))
			{
				q.setInteger(counter, Integer.parseInt(hPlaceHolderObject.getValue() + ""));
			}
			else if (objectType.equals("long"))
			{
				q.setLong(counter, Long.parseLong(hPlaceHolderObject.getValue() + ""));
			}
		}
		return q;

	}

}