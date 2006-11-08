
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
import net.sf.hibernate.Session;
import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DateAttribute;
import edu.common.dynamicextensions.domain.DoubleAttribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.FloatAttribute;
import edu.common.dynamicextensions.domain.IntegerAttribute;
import edu.common.dynamicextensions.domain.LongAttribute;
import edu.common.dynamicextensions.domain.ShortAttribute;
import edu.common.dynamicextensions.domain.StringAttribute;
import edu.common.dynamicextensions.domain.databaseproperties.ColumnProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
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
	private static EntityManager entityManager = null;

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
	public static synchronized EntityManager getInstance()
	{
		if (entityManager == null)
		{
			entityManager = new EntityManager();
		}
		return entityManager;
	}

	/**
	 * Mock entity manager can be placed in the entity manager using this method.
	 * @param entityManager
	 */
	public void setInstance(EntityManager entityManager)
	{
		EntityManager.entityManager = entityManager;

	}

	private void logDebug(String methodName, String message)
	{
		Logger.out.debug("[EntityManager.]" + methodName + "()--" + message);
	}

	/**
	 * Creates an Entity with the given entity information.Entity is registered 
	 * in the metadata and a table is created to store the records.
	 * @param entityInterface the entity to be created.
	 * @throws DynamicExtensionsSystemException system exception 
	 * @throws DynamicExtensionsApplicationException application exception
	 * @return EntityInterface entity interface
	 * 
	 */
	public EntityInterface createEntity(EntityInterface entityInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		logDebug("createEntity", "Entering method");
		Entity entity = (Entity) entityInterface;
		entity = saveOrUpdateEntity(entityInterface, true);
		logDebug("createEntity", "Exiting method");
		return entity;
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
	 */
	private void postSaveProcessEntity(Entity entity)
	{
		if (entity.getTableProperties() == null)
		{
			TableProperties tableProperties = new TableProperties();
			String tableName = TABLE_NAME_PREFIX + UNDERSCORE + entity.getId();
			tableProperties.setName(tableName);
			entity.setTableProperties(tableProperties);
		}
		Collection attributeCollection = entity.getAttributeCollection();
		entity.setLastUpdated(new Date());
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator iterator = attributeCollection.iterator();
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
			}
		}
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
	 * @param reverseQueryList2 
	 * @param queryList2 
	 * @param hibernateDAO 
	 * @param session Hibernate Session through which connection is obtained to fire the queries.
	 * @throws DynamicExtensionsSystemException Whenever there is any exception , this exception is thrown with proper message and the exception is 
	 * wrapped inside this exception.
	 */
	private Stack executeDataTableQueries(Entity entity, List queryList, List reverseQueryList)
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

		Stack reverseQuerysubStack = new Stack();
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
							reverseQuerysubStack.push(reverseQueryListIterator.next());
						}
					}
					catch (SQLException e)
					{
						rollbackQueries(reverseQuerysubStack, conn, entity);
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

		return reverseQuerysubStack;

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
	private void rollbackQueries(Stack reverseQueryList, Connection conn, Entity entity)
			throws DynamicExtensionsSystemException
	{
		if (reverseQueryList != null && !reverseQueryList.isEmpty())
		{

			while (!reverseQueryList.empty())
			{
				String query = (String) reverseQueryList.pop();
				PreparedStatement statement = null;
				try
				{
					statement = conn.prepareStatement(query);
					statement.executeUpdate();
				}
				catch (SQLException e)
				{
					LogFatalError(e, entity);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(
							"Queries rolled back....Could not create data table for the entity"
									+ entity);
					ex.setErrorCode(DYEXTN_S_000);
					throw ex;
				}

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
	private void LogFatalError(SQLException e, Entity entity)
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
	 * @return List of all the data table queries
	 * @throws DynamicExtensionsSystemException 
	 */
	private List getQueryList(Entity entity, List reverseQueryList)
			throws DynamicExtensionsSystemException
	{
		List queryList = new ArrayList();
		String mainTableQuery = getEntityMainDataTableQuery(entity, reverseQueryList);
		List associationTableQueryList = getDataTableQueriesForAssociationsInEntity(entity,
				reverseQueryList);
		queryList.add(mainTableQuery);
		queryList.addAll(associationTableQueryList);
		return queryList;
	}

	/**
	 * This method returns all the CREATE table entries for associations present in the entity.
	 * @param entity Entity object from which to get the associations.
	 * @param reverseQueryList Reverse query list that holds the reverse queries.
	 * @return
	 */
	private List getDataTableQueriesForAssociationsInEntity(Entity entity, List reverseQueryList)
	{
		// TODO Auto-generated method stub
		return new ArrayList();
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
				AbstractAttribute attribute = (AbstractAttribute) attributeIterator.next();
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
				attributeQuery = getQueryPartForAssociation((Association) attribute);
			}
		}
		return attributeQuery;
	}

	/**
	 * This method builds the query part for the association.
	 * @param association Association object for which to build the query.
	 * @return String query part of the association.
	 */
	private String getQueryPartForAssociation(Association association)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method builds the query part for the primitive attribute 
	 * @param attribute primitive attribute for which to build the query.
	 * @return String query part of the primitive attribute.
	 * @throws DataTypeFactoryInitializationException 
	 */
	private String getQueryPartForAttribute(Attribute attribute, boolean processUniqueConstraint)
			throws DynamicExtensionsSystemException
	{

		//TODO IS UNIQUE AND NOT NULL TO BE ADDED HERE 
		String attributeQuery = null;
		if (attribute != null)
		{
			String columnName = attribute.getColumnProperties().getName();
			String isUnique = "";
			String nullConstraint = "";
			if (processUniqueConstraint && attribute.getIsPrimaryKey())
			{

				isUnique = CONSTRAINT_KEYWORD + WHITESPACE
						+ attribute.getColumnProperties().getName() + UNDERSCORE
						+ UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE + UNIQUE_KEYWORD;

				nullConstraint = "NULL";

				if (!attribute.getIsNullable())
				{
					nullConstraint = "NOT NULL";
				}
			}

			attributeQuery = columnName + " " + getDatabaseTypeAndSize(attribute) + WHITESPACE
					+ isUnique + WHITESPACE + nullConstraint;
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
			if (attribute instanceof StringAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("String");
			}
			else if (attribute instanceof IntegerAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Integer");
			}
			else if (attribute instanceof DateAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Date");
			}
			else if (attribute instanceof FloatAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Float");
			}
			else if (attribute instanceof FloatAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Boolean");
			}
			else if (attribute instanceof DoubleAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Double");
			}
			else if (attribute instanceof LongAttribute)
			{
				return dataTypeFactory.getDatabaseDataType("Long");
			}
			else if (attribute instanceof ShortAttribute)
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
	 * Returns an entity object given the entity name; 
	 * @param entityName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityInterface getEntityByName(String entityName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
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
	 * Returns all entities in the whole system
	 * @return Collection Entity Collection
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getAllEntities() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		return getAllObjects(EntityInterface.class.getName());
	}

	@SuppressWarnings("unchecked")
	public Collection<ContainerInterface> getAllContainers()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return getAllObjects(ContainerInterface.class.getName());
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
	 * Returns an attribute given the entity name and attribute name
	 * @param entityName
	 * @param attributeName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public AttributeInterface getAttribute(String entityName, String attributeName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns an association object given the entity name and source role name
	 * @param entityName
	 * @param sourceRoleName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */

	public AssociationInterface getAssociation(String entityName, String sourceRoleName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns a collection of association objects given the source entity name and
	 * target entity name
	 * @param sourceEntityName
	 * @param targetEntityName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getAssociations(String sourceEntityName, String targetEntityName)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
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
	 *  Returns a collection of entities given the entity concept code. 
	 * @param entityConceptCode
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public Collection getEntitiesByConceptCode(String entityConceptCode)
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
	public ContainerInterface createContainer(ContainerInterface containerInterface)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Container container = (Container) containerInterface;
		Entity entity = (Entity) container.getEntity();
		if (container == null)
		{
			throw new DynamicExtensionsSystemException("Container passed is null");
		}
		else
		{
			HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
					Constants.HIBERNATE_DAO);
			Stack stack = null;
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
				preSaveProcessContainer(container);
				hibernateDAO.insert(container, null, false, false);
				if (container.getEntity() != null)
				{
					postSaveProcessEntity(entity);
					hibernateDAO.update(entity, null, false, false, false);
					List reverseQueryList = new LinkedList();
					List queryList = getQueryList(entity, reverseQueryList);

					stack = executeDataTableQueries(entity, queryList, reverseQueryList);
				}
				hibernateDAO.commit();
			}
			catch (DAOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
					if (stack != null)
					{
						rollbackQueries(stack, DBUtil.getConnection(), entity);
					}
				}
				catch (DAOException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the session", e1);
				}
				catch (DynamicExtensionsSystemException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the data table queries for the entity", e1);
				}
				catch (HibernateException e2)
				{
					throw new DynamicExtensionsSystemException(
							"Error while getting connection to roll back the session.", e2);
				}
			}
			catch (UserNotAuthorizedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
				}
				catch (DAOException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the session", e1);
				}
			}
			catch (DynamicExtensionsSystemException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
				}
				catch (DAOException e1)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new DynamicExtensionsSystemException(
							"Error while executing the data table queries for entity", e1);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new DynamicExtensionsSystemException("Error while closing the session", e);
				}
			}
		}

		return container;
	}

	/**
	 * This method is used to save the container into the database.
	 * @param containerInterface container to save
	 * @return ContainerInterface container Interface that is saved.
	 * @throws DynamicExtensionsSystemException Thrown if for any reason operation can not be completed.
	 * @throws DynamicExtensionsApplicationException Thrown if the entity name already exists.
	 * @throws DynamicExtensionsSystemException 
	 */
	public ContainerInterface editContainer(ContainerInterface containerInterface)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Container container = (Container) containerInterface;
		Entity entity = (Entity) container.getEntity();
		if (container == null)
		{
			throw new DynamicExtensionsSystemException("Container passed is null");
		}
		else
		{
			HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
					Constants.HIBERNATE_DAO);

			Stack stack = null;
			Entity databaseCopy = null;
			try
			{
				if (container.getEntity() != null)
				{
					Long id = entity.getId();
					hibernateDAO.openSession(null);
					List entityList = hibernateDAO.retrieve(Entity.class.getName(), Constants.ID,
							id);
					if (entityList != null && !entityList.isEmpty())
					{
						databaseCopy = (Entity) entityList.get(0);
					}
					hibernateDAO.closeSession();
				}
				hibernateDAO.openSession(null);
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException(
						"Exception occured while opening a session to save the container.");
			}
			try
			{
				preSaveProcessContainer(container);
				hibernateDAO.update(container, null, false, false, false);
				if (container.getEntity() != null)
				{
					postSaveProcessEntity(entity);
					hibernateDAO.update(entity, null, false, false, false);
					stack = executeUpdateDataTableQueries(entity, databaseCopy);
				}
				hibernateDAO.commit();
			}
			catch (DAOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
					if (stack != null)
					{
						rollbackQueries(stack, DBUtil.getConnection(), entity);
					}
				}
				catch (DAOException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the session", e1);
				}
				catch (DynamicExtensionsSystemException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the data table queries for the entity", e1);
				}
				catch (HibernateException e2)
				{
					throw new DynamicExtensionsSystemException(
							"Error while getting connection to roll back the session.", e2);
				}
			}
			catch (UserNotAuthorizedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
				}
				catch (DAOException e1)
				{
					throw new DynamicExtensionsSystemException(
							"Error while rolling back the session", e1);
				}
			}
			catch (DynamicExtensionsSystemException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try
				{
					hibernateDAO.rollback();
				}
				catch (DAOException e1)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new DynamicExtensionsSystemException(
							"Error while executing the data table queries for entity", e1);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new DynamicExtensionsSystemException("Error while closing the session", e);
				}
			}
		}

		return container;
	}

	private void preSaveProcessContainer(Container container)
	{
		if (container.getEntity() != null)
		{
			Entity entity = (Entity) container.getEntity();
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

		//        Map colNameMap = getDbColumnNameMap(entity);

		Set uiColumnSet = dataValue.keySet();
		Iterator uiColumnSetIter = uiColumnSet.iterator();

		while (uiColumnSetIter.hasNext())
		{
			AbstractAttribute attribute = (AbstractAttribute) uiColumnSetIter.next();
			if (attribute instanceof AttributeInterface)
			{
				columnNameString.append(" , ");
				columnValuesString.append(" , ");
				String dbColumnName = ((AttributeInterface) attribute).getColumnProperties()
						.getName();
				Object value = dataValue.get(attribute);

				columnNameString.append(dbColumnName);
				value = getFormattedValue(attribute, value);
				columnValuesString.append(value);

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

		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			jdbcDao.executeUpdate(query.toString());
			//jdbcDao.commit();
			jdbcDao.closeSession();
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while inserting data", e);
		}
		
		return identifier;
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
		if (attribute == null)
		{
			formattedvalue = null;
		}
		else if (attribute instanceof StringAttribute)
		{
			formattedvalue = "'" + value + "'";
		}
		else if (attribute instanceof DateAttribute)
		{
			formattedvalue = Variables.strTodateFunction + "('" + value + "','"
					+ Variables.datePattern + "')";
		}
		else
		{
			formattedvalue = value.toString();
		}
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
	 * This method is used to update the existing entity into the database. This method compares the edited entity with the database copy of that 
	 * entity and checks following differences.
	 * <BR>
	 * Newly added attributes. <BR>
	 * Updated attribute in terms of changed data type <BR> 
	 * Updated attributes in terms of changed (added/removed) constraints (UNIQUE, NOT NULL)<BR>
	 * @param entityInterface Edited entity interface 
	 * @return EntityInterface Saved entity
	 * @throws DynamicExtensionsSystemException This exception is thrown in case of any system error
	 * @throws DynamicExtensionsApplicationException This exception is thrown in case of any application error
	 */
	public EntityInterface editEntity(EntityInterface entityInterface)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Entity entity = (Entity) entityInterface;
		if (entityInterface == null)
		{
			throw new DynamicExtensionsSystemException("Entity is null...can not update");
		}
		else if (entityInterface.getId() == null)
		{
			return createEntity(entityInterface);
		}
		else
		{
			entity = saveOrUpdateEntity(entityInterface, false);
		}
		return entity;
	}

	/**
	 * This method is used by create as well as edit entity methods. This method holds all the common part 
	 * related to saving the entity into the database and also handling the exceptions .
	 * @param entityInterface Entity to be stored in the database.
	 * @param isNew flag for whether it is a save or update.
	 * @return Entity . Stored instance of the entity.
	 * @throws DynamicExtensionsApplicationException System exception in case of any fatal errors.
	 * @throws DynamicExtensionsSystemException Thrown in case of duplicate name or authentication failure.
	 */
	private Entity saveOrUpdateEntity(EntityInterface entityInterface, boolean isNew)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		logDebug("saveOrUpdateEntity", "Entering method");
		Entity entity = (Entity) entityInterface;
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		Stack stack = null;
		try
		{
			checkForDuplicateEntityName(entity);

			if (isNew)
			{
				hibernateDAO.openSession(null);
				hibernateDAO.insert(entity, null, false, false);
				postSaveProcessEntity(entity);
				hibernateDAO.update(entity, null, false, false, false);

				List reverseQueryList = new LinkedList();
				List queryList = getQueryList(entity, reverseQueryList);

				stack = executeDataTableQueries(entity, queryList, reverseQueryList);
			}
			else
			{
				Long id = entity.getId();
				hibernateDAO.openSession(null);
				List entityList = hibernateDAO.retrieve(Entity.class.getName(), Constants.ID, id);
				Entity databaseCopy = null;
				if (entityList != null && !entityList.isEmpty())
				{
					databaseCopy = (Entity) entityList.get(0);
				}
				hibernateDAO.closeSession();
				hibernateDAO.openSession(null);
				hibernateDAO.update(entity, null, false, false, false);
				postSaveProcessEntity(entity);
				hibernateDAO.update(entity, null, false, false, false);
				stack = executeUpdateDataTableQueries(entity, databaseCopy);
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
				if (stack != null)
				{
					rollbackQueries(stack, DBUtil.getConnection(), entity);
				}
			}
			catch (Exception e1)
			{
				throw new DynamicExtensionsSystemException(
						"Exception occured while rolling back the session", e1, DYEXTN_S_001);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}
		catch (DynamicExtensionsSystemException e)
		{
			try
			{
				hibernateDAO.rollback();
                if (stack != null)
                {
                    rollbackQueries(stack, DBUtil.getConnection(), entity);
                }
			}
			catch (Exception e1)
			{
				throw new DynamicExtensionsSystemException(
						"Exception occured while rolling back the session", e1, DYEXTN_S_001);
			}
			throw e;
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
		return entity;
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
	private Stack executeUpdateDataTableQueries(Entity entity, Entity databaseCopy)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		logDebug("executeUpdateDataTableQueries", "Entering method");
		Stack rollBackQueryStack = null;
		Collection attributeCollection = entity.getAbstractAttributeCollection();
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();
			List attributeQueryList = new ArrayList();
			List attributeRollbackQueryList = new ArrayList();
			while (attributeIterator.hasNext())
			{
				AbstractAttribute abstractAttribute = (AbstractAttribute) attributeIterator.next();
				AbstractAttribute abstractSavedAttribute = (AbstractAttribute) databaseCopy
						.getAttributeByIdentifier(abstractAttribute.getId());

				if (abstractAttribute instanceof Attribute)
				{
					Attribute attribute = (Attribute) abstractAttribute;
					Attribute savedAttribute = (Attribute) abstractSavedAttribute;

					if (savedAttribute == null)
					{
						String attributeQuery = processAddAttribute(attribute,
								attributeRollbackQueryList);
						logDebug("executeUpdateDataTableQueries", "Query "
								+ attributeQueryList.size() + "= " + attributeQuery);
						logDebug("executeUpdateDataTableQueries", "roll back Query "
								+ attributeRollbackQueryList
										.get(attributeRollbackQueryList.size() - 1));
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

			rollBackQueryStack = executeDataTableQueries(entity, attributeQueryList,
					attributeRollbackQueryList);
		}
		logDebug("executeUpdateDataTableQueries", "Exiting method");
		return rollBackQueryStack;
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

		String tableName = entity.getTableProperties().getName();
		String[] selectColumnName = new String[attributesCollection.size()];
		String[] whereColumnName = new String[]{IDENTIFIER};
		String[] whereColumnCondition = new String[]{"="};
		Object[] whereColumnValue = new Object[]{recordId};

		Iterator attriIterator = attributesCollection.iterator();
		Map columnNameMap = new HashMap();
		int index = 0;
		while (attriIterator.hasNext())
		{
			AttributeInterface attribute = (AttributeInterface) attriIterator.next();
			String dbColumnName = attribute.getColumnProperties().getName();
			String uiColumnName = attribute.getName();
			selectColumnName[index] = dbColumnName;
			columnNameMap.put(dbColumnName, uiColumnName);
			index++;
		}

		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
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

			jdbcDao.closeSession();
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while retrieving the data", e);
		}
		return recordValues;
	}
}