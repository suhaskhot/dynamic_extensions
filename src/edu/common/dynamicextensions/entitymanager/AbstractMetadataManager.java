
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.bizlogic.AbstractBizLogic;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author mandar_shidhore
 * @author rajesh_patil
 *
 */
public abstract class AbstractMetadataManager
		implements
			EntityManagerExceptionConstantsInterface,
			DynamicExtensionsQueryBuilderConstantsInterface
{

	/**
	 * This method creates dynamic table queries for the entities within a group.
	 * @param dyExtBsDmnObj
	 * @param revQueries
	 * @param queries
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected abstract void preProcess(DynamicExtensionBaseDomainObjectInterface dyExtBsDmnObj,
			List<String> revQueries, List<String> queries) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException;

	/**
	 * This method executes dynamic table queries created for all the entities within a group.
	 * @param queries List of queries to be executed to created dynamic tables.
	 * @param revQueries List of queries to be executed in case any problem occurs at DB level.
	 * @param rlbkQryStack Stack to undo any changes done beforehand at DB level.
	 * @throws DynamicExtensionsSystemException
	 */
	protected abstract void postProcess(List<String> queries, List<String> revQueries,
			Stack<String> rlbkQryStack) throws DynamicExtensionsSystemException;

	/**
	 * This method is called when exception occurs while executing the roll back queries
	 * or reverse queries. When this method is called, it signifies that the database state
	 * and the meta data state for the entity are not in synchronization and administrator
	 * needs some database correction.
	 * @param exception The exception that took place.
	 * @param abstrMetadata Entity for which data tables are out of sync.
	 */
	protected abstract void logFatalError(Exception exception,
			AbstractMetadataInterface abstrMetadata);

	/**
	 * LogFatalError.
	 * @param e
	 * @param abstractMetadata
	 */
	protected abstract DynamicExtensionBaseQueryBuilder getQueryBuilderInstance();

	/**
	 * This method takes the class name, object name and returns the object.
	 * @param className class name
	 * @param objectName objectName
	 * @return DynamicExtensionBaseDomainObjectInterface
	 */
	public DynamicExtensionBaseDomainObjectInterface getObjectByName(String className,
			String objectName) throws DynamicExtensionsSystemException
	{
		DynamicExtensionBaseDomainObjectInterface dyExtBsDmnObj = null;

		if (objectName == null || objectName.equals(""))
		{
			return dyExtBsDmnObj;
		}

		// Get the instance of the default biz logic.
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		List objects = new ArrayList();

		try
		{
			objects = defaultBizLogic.retrieve(className, "name", objectName);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		if (objects != null && objects.size() > 0)
		{
			dyExtBsDmnObj = (DynamicExtensionBaseDomainObjectInterface) objects.get(0);
		}

		return dyExtBsDmnObj;
	}

	/**
	 * Returns all instances in the whole system for a given type of the object
	 * @param objectName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected Collection getAllObjects(String objectName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		Collection objects = new HashSet();

		try
		{
			objects = bizLogic.retrieve(objectName);
			if (objects == null)
			{
				objects = new HashSet();
			}
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		return objects;
	}

	/**
	 * This method returns object for a given class name and identifier
	 * @param objectName
	 * @param identifier
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected DynamicExtensionBaseDomainObject getObjectByIdentifier(String objectName,
			String identifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		DynamicExtensionBaseDomainObject dyExtBsDmnObj;
		try
		{
			// After moving to MYSQL 5.2, the type checking is strict so changing the identifier to Long.
			List objects = bizLogic.retrieve(objectName, Constants.ID, new Long(identifier));

			if (objects == null || objects.size() == 0)
			{
				Logger.out.debug("Required Obejct not found: Object Name*" + objectName
						+ "*   identifier  *" + identifier + "*");
				System.out.println("Required Obejct not found: Object Name*" + objectName
						+ "*   identifier  *" + identifier + "*");
				throw new DynamicExtensionsApplicationException("OBJECT_NOT_FOUND");
			}

			dyExtBsDmnObj = (DynamicExtensionBaseDomainObject) objects.get(0);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		return dyExtBsDmnObj;
	}

	/**
	 * This method is called when there any exception occurs while generating the data table queries
	 * for the entity. Valid scenario is that if we need to fire Q1 Q2 and Q3 in order to create the
	 * data tables and Q1 Q2 get fired successfully and exception occurs while executing query Q3 then
	 * this method receives the query list which holds the set of queries which negate the effect of the
	 * queries which were generated successfully so that the meta data information and database are in
	 * synchronization.
	 * @param revQryStack
	 * @param abstrMetadata
	 * @param exception
	 * @param dao
	 * @throws DynamicExtensionsSystemException
	 */
	protected void rollbackQueries(Stack<String> revQryStack,
			AbstractMetadataInterface abstrMetadata, Exception exception, AbstractDAO dao)
			throws DynamicExtensionsSystemException
	{
		String message = "";

		dao.rollback();

		if (revQryStack != null && !revQryStack.isEmpty())
		{
			Connection conn = null;
			try
			{
				conn = DBUtil.getConnection();
				while (!revQryStack.empty())
				{
					String query = (String) revQryStack.pop();
					PreparedStatement statement = null;
					statement = conn.prepareStatement(query);
					try
					{
						statement.executeUpdate();
					}
					catch (SQLException e)
					{
						throw new DynamicExtensionsSystemException(
								"Exception occured while executing rollback queries.", e,
								DYEXTN_S_002);
					}
					finally
					{
						try
						{
							statement.close();
						}
						catch (SQLException e)
						{
							e.printStackTrace();
							throw new DynamicExtensionsSystemException(
									"Exception occured while closing statement", e);
						}
					}
				}
			}
			catch (HibernateException e)
			{
				message = e.getMessage();
			}
			catch (SQLException exc)
			{
				message = exc.getMessage();
				logFatalError(exc, abstrMetadata);
			}
			finally
			{
				try
				{
					conn.commit();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}

				logDebug("rollbackQueries", DynamicExtensionsUtility.getStackTrace(exception));
				DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(message,
						exception);

				ex.setErrorCode(DYEXTN_S_000);
				throw ex;
			}
		}
	}

	/**
	 * This method executes the HQL query given the query name and query parameters.
	 * The queries are specified in the EntityManagerHQL.hbm.xml file. For each query, a name is given.
	 * Each query is replaced with parameters before execution.The parameters are given by each calling 
	 * method.
	 * @param queryName
	 * @param substParams
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected Collection executeHQL(String queryName, Map<String, HQLPlaceHolderObject> substParams)
			throws DynamicExtensionsSystemException
	{
		Collection objects = new HashSet();

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);

		try
		{
			hibernateDAO.openSession(null);
			Session session = DBUtil.currentSession();
			Query query = substitutionParameterForQuery(session, queryName, substParams);
			objects = query.list();
		}
		catch (Exception e)
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
				throw new DynamicExtensionsSystemException(
						"Exception occured while closing the session", e, DYEXTN_S_001);
			}

		}

		return objects;
	}

	/**
	 * This method substitutes the parameters from substitution parameters map into the input query.
	 * @param session
	 * @param queryName
	 * @param substParams
	 * @return
	 * @throws HibernateException
	 */
	protected Query substitutionParameterForQuery(Session session, String queryName,
			Map<String, HQLPlaceHolderObject> substParams) throws HibernateException
	{
		Query query = session.getNamedQuery(queryName);

		for (int counter = 0; counter < substParams.size(); counter++)
		{
			HQLPlaceHolderObject plcHolderObj = (HQLPlaceHolderObject) substParams
					.get(counter + "");
			String objectType = plcHolderObj.getType();
			if (objectType.equals("string"))
			{
				query.setString(counter, plcHolderObj.getValue() + "");
			}
			else if (objectType.equals("integer"))
			{
				query.setInteger(counter, Integer.parseInt(plcHolderObj.getValue() + ""));
			}
			else if (objectType.equals("long"))
			{
				query.setLong(counter, Long.parseLong(plcHolderObj.getValue() + ""));
			}
			else if (objectType.equals("boolean"))
			{
				query.setBoolean(counter, Boolean.parseBoolean(plcHolderObj.getValue() + ""));
			}
		}

		return query;
	}

	/**
	*
	* @param hibernateDAO
	* @param queryName
	* @param substParams
	* @return
	* @throws DynamicExtensionsSystemException
	*/
	protected Collection executeHQL(HibernateDAO hibernateDAO, String queryName,
			Map<String, HQLPlaceHolderObject> substParams) throws DynamicExtensionsSystemException
	{
		Collection entities = new HashSet();

		try
		{
			Session session = DBUtil.currentSession();
			Query query = substitutionParameterForQuery(session, queryName, substParams);
			entities = query.list();
		}
		catch (HibernateException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}

		return entities;
	}

	/**
	 * This method persists an entity group and the associated entities and also creates the data table
	 * for the entities.
	 * @param abstrMetadata
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected AbstractMetadataInterface persistDynamicExtensionObject(
			AbstractMetadataInterface abstrMetadata) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List<String> revQueries = new LinkedList<String>();
		List<String> queries = new ArrayList<String>();
		Stack<String> rlbkQryStack = new Stack<String>();

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			preProcess(abstrMetadata, revQueries, queries);

			saveDynamicExtensionObject(abstrMetadata, hibernateDAO, rlbkQryStack);

			postProcess(queries, revQueries, rlbkQryStack);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
		catch (DynamicExtensionsSystemException e)
		{
			rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (Exception e)
			{
				rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			}
		}

		return abstrMetadata;
	}

	/**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param abstrMetadata
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public AbstractMetadataInterface persistDynamicExtensionObjectMetdata(
			AbstractMetadataInterface abstrMetadata) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Stack<String> rlbkQryStack = new Stack<String>();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			saveDynamicExtensionObject(abstrMetadata, hibernateDAO, rlbkQryStack);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
			}
		}

		return abstrMetadata;
	}

	/**
	 * @param abstrMetadata
	 * @param hibernateDAO
	 * @param rlbkQryStack
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void saveDynamicExtensionObject(AbstractMetadataInterface abstrMetadata,
			HibernateDAO hibernateDAO, Stack<String> rlbkQryStack)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			if (abstrMetadata.getId() == null)
			{
				hibernateDAO.insert(abstrMetadata, null, false, false);
			}
			else
			{
				hibernateDAO.update(abstrMetadata, null, false, false, false);
			}
		}
		catch (DAOException e)
		{
			rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
		catch (UserNotAuthorizedException e)
		{
			rollbackQueries(rlbkQryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
	}

	/**
	 * This method is used to log the messages in a uniform manner. The method takes the string method name and
	 * string message. Using these parameters the method formats the message and logs it.
	 * @param methodName Name of the method for which the message needs to be logged.
	 * @param message The message that needs to be logged.
	 */
	protected void logDebug(String methodName, String message)
	{
		Logger.out.debug("[AbstractMetadataManager.]" + methodName + "() -- " + message);
	}

	/**
	 * @param exception
	 * @param excMessage
	 * @param dao
	 * @param isExcToBeWrpd
	 * @return
	 */
	protected Exception handleRollback(Exception exception, String excMessage, AbstractDAO dao,
			boolean isExcToBeWrpd)
	{
		dao.rollback();

		if (isExcToBeWrpd)
		{
			return new DynamicExtensionsSystemException(excMessage, exception);
		}
		else
		{
			return exception;
		}
	}

	/**
	 * @param entityGroup
	 * @param revQueries
	 * @param queries
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected List<String> getDynamicQueryList(EntityGroupInterface entityGroup,
			List<String> revQueries, List<String> queries) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List<EntityInterface> entities = DynamicExtensionsUtility.getUnsavedEntities(entityGroup);

		for (EntityInterface entity : entities)
		{
			List<String> createQueries = getQueryBuilderInstance().getCreateEntityQueryList(
					(Entity) entity, revQueries);

			if (createQueries != null && !createQueries.isEmpty())
			{
				queries.addAll(createQueries);
			}
		}

		for (EntityInterface entity : entities)
		{
			List<String> updateQueries = getQueryBuilderInstance().getUpdateEntityQueryList(
					(Entity) entity, revQueries);

			if (updateQueries != null && !updateQueries.isEmpty())
			{
				queries.addAll(updateQueries);
			}
		}

		List<EntityInterface> savedEntities = DynamicExtensionsUtility
				.getSavedEntities(entityGroup);

		for (EntityInterface savedEntity : savedEntities)
		{
			Entity dbaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, savedEntity.getId());

			List<String> updateQueries = getQueryBuilderInstance().getUpdateEntityQueryList(
					(Entity) savedEntity, dbaseCopy, revQueries);

			if (updateQueries != null && !updateQueries.isEmpty())
			{
				queries.addAll(updateQueries);
			}
		}

		return queries;
	}

	/**
	 * @param entity
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public List<EntityRecord> getAllRecords(AbstractEntityInterface entity)
			throws DynamicExtensionsSystemException
	{
		List<EntityRecord> records = new ArrayList<EntityRecord>();
		JDBCDAO jdbcDao = null;
		List<List> results;

		try
		{
			jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			TablePropertiesInterface tblProperties = entity.getTableProperties();
			String tableName = tblProperties.getName();
			String[] selectColName = {IDENTIFIER};
			String[] whereColName = {Constants.ACTIVITY_STATUS_COLUMN};
			String[] whereColCndtn = {EQUAL};
			Object[] whereColValue = {"'" + Constants.ACTIVITY_STATUS_ACTIVE + "'"};
			results = jdbcDao.retrieve(tableName, selectColName, whereColName, whereColCndtn,
					whereColValue, null);
			records = getRecordList(results);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while retrieving the data", e);
		}
		finally
		{
			try
			{
				jdbcDao.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("Error while retrieving the data", e);
			}
		}

		return records;
	}

	/**
	* @param results
	* @return
	*/
	protected List<EntityRecord> getRecordList(List<List> results)
	{
		List<EntityRecord> records = new ArrayList<EntityRecord>();
		EntityRecord entityRecord;
		String id;

		for (List innnerList : results)
		{
			if (innnerList != null && !innnerList.isEmpty())
			{
				id = (String) innnerList.get(0);
				if (id != null)
				{
					entityRecord = new EntityRecord(new Long(id));
					records.add(entityRecord);
				}
			}
		}

		return records;
	}

}