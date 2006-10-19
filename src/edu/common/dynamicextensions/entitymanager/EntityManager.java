
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DateAttribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.FloatAttribute;
import edu.common.dynamicextensions.domain.IntegerAttribute;
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
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;


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
 * @author geetika_bangard
 */
public class EntityManager implements EntityManagerInterface
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

	/**
	 * Creates an Entity with the given entity information.Entity is registered 
	 * in the metadata and a table is created to store the records.
	 * @param entityInterface the entity to be created.
	 * @throws DynamicExtensionsSystemException system exception 
	 * @throws DynamicExtensionsApplicationException application exception
	 * @return EntityInterface entity interface
	 * 
	 */
	public EntityInterface createEntity(EntityInterface entityInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Entity entity = (Entity) entityInterface;
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getDAO(Constants.HIBERNATE_DAO);
		Stack stack = null;
		try
		{
			checkForDuplicateEntityName(entity);

			try
			{
				hibernateDAO.openSession(null);
				hibernateDAO.insert(entity, null, false, false);
				postSaveProcessEntity(entity);
				hibernateDAO.update(entity, null, false, false, false);
				stack = executeDataTableQueries(entity);
			}

			catch (UserNotAuthorizedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//            Transaction trans = session.beginTransaction();
			//            session.save(entity);
			//            postSaveProcessEntity(entity);
			//            session.update(entity);

			//            trans.commit();
			hibernateDAO.commit();
		}
		// catch (HibernateException e)
		catch (DAOException e)
		{
			try
			{
				// session.connection().rollback();
				hibernateDAO.rollback();
				if (stack != null)
				{
					rollbackQueries(stack, DBUtil.getConnection(), entity);
				}
			}
			catch (Exception e1)
			{
				throw new DynamicExtensionsSystemException("Exception occured while rolling back the session", e1);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		catch (DynamicExtensionsSystemException e)
		{
			try
			{
				//session.connection().rollback();
				hibernateDAO.rollback();
			}
			catch (Exception e1)
			{
				throw new DynamicExtensionsSystemException("Exception occured while rolling back the session", e1);
			}
			throw new DynamicExtensionsSystemException("Exception occured while creating data table for entity", e);
		}
		finally
		{

			//session.close();
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				e.printStackTrace();
				throw new DynamicExtensionsSystemException("Exception occured while closing the session", e);
			}

		}
		return entity;
	}

	/**
	 * This method populates the TableProperties object in entity which holds the unique tablename for the entity. This 
	 * table name is generated using the unique identifier that is generated after saving the object. The format for generating 
	 * this table/column name is "DYNEXT_<ENTITY/ATTRIBUTE/ASSOCIATION>_<UNIQUE IDENTIFIER>"
	 * So we need this method to generate the table name for the entity then create the corresponding tableProperties 
	 * object and then update the entity object with this newly added tableProperties object. 
	 * Similarly we add ColumnProperties object for each of the attribute and also the ConstraintProperties object 
	 * for each of the associations.
	 * @param entity Entity object on which to process the post save operations.
	 */
	private void postSaveProcessEntity(Entity entity)
	{
		TableProperties tableProperties = new TableProperties();
		String tableName = "DYNEXT_ENTITY_" + entity.getId();
		tableProperties.setName(tableName);
		entity.setTableProperties(tableProperties);
		Collection attributeCollection = entity.getAttributeCollection();
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator iterator = attributeCollection.iterator();
			while (iterator.hasNext())
			{
				AbstractAttribute attribute = (AbstractAttribute) iterator.next();

				ColumnProperties colProperties = new ColumnProperties();
				String colName = "DYNEXT_ATTRIBUTE_" + attribute.getId();
				colProperties.setName(colName);
				if (attribute instanceof Attribute)
				{
					((Attribute) attribute).setColumnProperties(colProperties);

				}
			}
		}
	}

	/**
	 * This method checks if the entity can be created with the given name or not. This method will fire a query to check the uniqueness 
	 * of the given name of the entity and accodingly operations will proceed.
	 * @param entity Entity whose name's uniqueness is to be checked.
	 * @throws DynamicExtensionsApplicationException This will basically act as a duplicate name  exception.
	 */
	private void checkForDuplicateEntityName(Entity entity) throws DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * This method executes the queries which generate and or manipulate the data table associated with the entity.
	 * @param entity Entity for which the data table queries are to be executed.
	 * @param hibernateDAO 
	 * @param session Hibernate Session through which connection is obtained to fire the queries.
	 * @throws DynamicExtensionsSystemException Whenever there is any exception , this exception is thrown with proper message and the exception is 
	 * wrapped inside this exception.
	 */
	private Stack executeDataTableQueries(Entity entity) throws DynamicExtensionsSystemException
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
			throw new DynamicExtensionsSystemException("Unable to exectute the data table queries .....Cannot access sesssion", e1);
		}
		List reverseQueryList = new LinkedList();
		List queryList = getQueryList(entity, reverseQueryList);
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
						throw new DynamicExtensionsSystemException("Exception occured while executing the data table query", e);
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
						throw new DynamicExtensionsSystemException("Exception occured while forming the data tables for entity", e);
					}
				}
			}
		}
		catch (HibernateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	private void rollbackQueries(Stack reverseQueryList, Connection conn, Entity entity) throws DynamicExtensionsSystemException
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
				finally
				{
					throw new DynamicExtensionsSystemException("Queries rolled back....Could not create data table for the entity" + entity);
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
		// TODO Auto-generated method stub

	}

	/**
	 * This method builds the list of all the queries that need to be executed in order to create the data table for the entity and its associations.
	 * @param entity Entity for which to get the queries.
	 * @param reverseQueryList For every data table query the method builds one more query which negats the effect of that data table query. All such
	 * reverse queries are added in this list.
	 * @return List of all the data table queries
	 * @throws DynamicExtensionsSystemException 
	 */
	private List getQueryList(Entity entity, List reverseQueryList) throws DynamicExtensionsSystemException
	{
		List queryList = new ArrayList();
		String mainTableQuery = getEntityMainDataTableQuery(entity, reverseQueryList);
		List associationTableQueryList = getDataTableQueriesForAssociationsInEntity(entity, reverseQueryList);
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
	private String getEntityMainDataTableQuery(Entity entity, List reverseQueryList) throws DynamicExtensionsSystemException
	{
		String dataType = getDataTypeForIdentifier();
		String tableName = entity.getTableProperties().getName();
		StringBuffer query = new StringBuffer("CREATE TABLE " + tableName + "( IDENTIFIER " + dataType + "(19,0) not null, ");
		Collection attributeCollection = entity.getAttributeCollection();
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();
			while (attributeIterator.hasNext())
			{
				AbstractAttribute attribute = (AbstractAttribute) attributeIterator.next();
				String attributeQueryPart = getQueryPartForAbstractAttribute(attribute);
				query = query.append(attributeQueryPart);
				query = query.append(",");
			}
		}
		query = query.append("primary key (IDENTIFIER))");
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
	private String getQueryPartForAbstractAttribute(AbstractAttribute attribute) throws DynamicExtensionsSystemException
	{
		String attributeQuery = null;
		if (attribute != null)
		{
			if (attribute instanceof Attribute)
			{
				try
				{
					attributeQuery = getQueryPartForAttribute((Attribute) attribute);
				}
				catch (DataTypeFactoryInitializationException e)
				{
					throw new DynamicExtensionsSystemException("Exception occured while retrieving the database type of the attribute");
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
	private String getQueryPartForAttribute(Attribute attribute) throws DataTypeFactoryInitializationException
	{
		String attributeQuery = null;
		if (attribute != null)
		{
			String columnName = attribute.getColumnProperties().getName();
			attributeQuery = columnName + " " + getDatabaseTypeAndSize(attribute);
		}
		return attributeQuery;
	}

	/**
	 * This method returns the database type and size of the attribute passed to it which becomes the part of the query for that attribute.
	 * @param attribute Attribute object for which to get the database type and size.
	 * @return String that specifies the data base type and size.
	 * @throws DataTypeFactoryInitializationException 
	 */
	private String getDatabaseTypeAndSize(Attribute attribute) throws DataTypeFactoryInitializationException
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

		return null;
	}

	/**
	 * This method returns the dabase type for idenitifier.
	 * @return String database type for the identifier.
	 */
	private String getDataTypeForIdentifier()
	{
		// TODO Auto-generated method stub
		return "number";
	}

	/**
	 * Returns an entity object given the entity name; 
	 * @param entityName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityInterface getEntityByName(String entityName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
	public Collection getEntitiesByAttributeName(String attributeName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return null;
	}

	/**
	 * Returns all entities in the whole system
	 * @return Collection Entity Collection
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
    
    public Collection getAllEntities()
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
    	return getAllObjects(EntityInterface.class.getName());
    }
    
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getEntityByIdentifier(java.lang.String)
	 */
	public EntityInterface getEntityByIdentifier(String identifier) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
    	return (EntityInterface) getObjectByIdentifier(EntityInterface.class.getName(),identifier);
	}
	
	/**
	 * This method returns object for a given class name and identifer 
	 * @param objectName  name of the class of the object
	 * @param identifier identifier of the object
	 * @return  obejct
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private AbstractMetadataInterface getObjectByIdentifier(String objectName,String identifier) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException {
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
    	AbstractMetadataInterface object;
		try
		{
			List objectList = bizLogic.retrieve(objectName,Constants.ID,identifier);
			
			if (objectList == null || objectList.size() == 0) {
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
    private Collection getAllObjects(String objectName)
    throws DynamicExtensionsSystemException,DynamicExtensionsApplicationException
    {
    	AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
    	Collection objectList = new HashSet();
		try
		{
			objectList = bizLogic.retrieve(objectName);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(),e);
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
	public AttributeInterface getAttribute(String entityName, String attributeName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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

	public AssociationInterface getAssociation(String entityName, String sourceRoleName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getAssociations(String sourceEntityName, String targetEntityName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getEntityByDescription(String entityDescription) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
	public Collection getEntitiesByAttributeDescription(String attributeDescription) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getEntitiesByConceptCode(String entityConceptCode) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getEntitiesByConceptName(String entityConceptName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getEntitiesByAttributeConceptCode(String attributeConceptCode) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public Collection getEntitiesByAttributeConceptName(String attributeConceptName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
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
	public ContainerInterface createContainer(ContainerInterface containerInterface) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		Container container = (Container) containerInterface;
		Entity entity = (Entity) container.getEntity();
		if (container == null)
		{
			throw new DynamicExtensionsSystemException("Container passed is null");
		}
		else
		{
			HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getDAO(Constants.HIBERNATE_DAO);
			Stack stack = null;
			try
			{
				hibernateDAO.openSession(null);
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Exception occured while opening a session to save the container.");
			}
			try
			{
				hibernateDAO.insert(container, null, false, false);
				if (container.getEntity() != null)
				{
					postSaveProcessEntity(entity);
					hibernateDAO.update(entity, null, false, false, false);
					stack = executeDataTableQueries(entity);
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
					throw new DynamicExtensionsSystemException("Error while rolling back the session", e1);
				}
				catch (DynamicExtensionsSystemException e1)
				{
					throw new DynamicExtensionsSystemException("Error while rolling back the data table queries for the entity", e1);
				}
				catch (HibernateException e2)
				{
					throw new DynamicExtensionsSystemException("Error while getting connection to roll back the session.", e2);
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
					throw new DynamicExtensionsSystemException("Error while rolling back the session", e1);
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
					throw new DynamicExtensionsSystemException("Error while executing the data table queries for entity", e1);
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

}