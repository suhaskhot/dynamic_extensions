
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
public abstract class AbstractMetadataManager implements EntityManagerExceptionConstantsInterface, DynamicExtensionsQueryBuilderConstantsInterface
{
	/**
	 * This method creates dynamic table queries for the entities within a group.
	 * @param group EntityGroup
	 * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
	 * @param hibernateDAO
	 * @param queryList List of queries to be executed to created dynamicn tables.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected abstract void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List<String> reverseQueryList,
			HibernateDAO hibernateDAO, List<String> queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * This method executes dynamic table queries created for all the entities within a group.
	 * @param queryList List of queries to be executed to created dynamicn tables.
	 * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
	 * @param rollbackQueryStack Stack to undo any changes done beforehand at DB level.
	 * @throws DynamicExtensionsSystemException
	 */
	protected abstract void postProcess(List<String> queryList, List<String> reverseQueryList, Stack rollbackQueryStack, HibernateDAO hibernateDAO)
			throws DynamicExtensionsSystemException;

	/**
	 * LogFatalError.
	 * @param e
	 * @param abstractMetadata
	 */
	protected abstract void LogFatalError(Exception e, AbstractMetadataInterface abstractMetadata);

	/**
	 * LogFatalError.
	 * @param e
	 * @param abstractMetadata
	 */
	protected abstract DynamicExtensionBaseQueryBuilder getQueryBuilderInstance();

	/**
	 * This method takes the class name , criteria for the object and returns the object.
	 * @param className class name
	 * @param objectName objectName
	 * @return DynamicExtensionBaseDomainObjectInterface
	 */
	public DynamicExtensionBaseDomainObjectInterface getObjectByName(String className, String objectName) throws DynamicExtensionsSystemException
	{
		DynamicExtensionBaseDomainObjectInterface object = null;

		if (objectName == null || objectName.equals(""))
		{
			return object;
		}

		//Getting the instance of the default biz logic.
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		List objectList = new ArrayList();

		try
		{
			objectList = defaultBizLogic.retrieve(className, "name", objectName);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

		if (objectList != null && objectList.size() > 0)
		{
			object = (DynamicExtensionBaseDomainObjectInterface) objectList.get(0);
		}

		return object;
	}

	/**
	 * Returns all instances in the whole system for a given type of the object
	 * @return Collection of instances of given class
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected Collection getAllObjects(String objectName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
	 * This method returns object for a given class name and identifer
	 * @param objectName  name of the class of the object
	 * @param identifier identifier of the object
	 * @return  obejct
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected DynamicExtensionBaseDomainObject getObjectByIdentifier(String objectName, String identifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		AbstractBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		DynamicExtensionBaseDomainObject object;
		try
		{
			//After moving to MYSQL 5.2 the type checking is strict so changing the identifier to Long
			List objectList = bizLogic.retrieve(objectName, Constants.ID, new Long(identifier));

			if (objectList == null || objectList.size() == 0)
			{
				Logger.out.debug("Required Obejct not found: Object Name*" + objectName + "*   identifier  *" + identifier + "*");
				System.out.println("Required Obejct not found: Object Name*" + objectName + "*   identifier  *" + identifier + "*");
				throw new DynamicExtensionsApplicationException("OBJECT_NOT_FOUND");
			}

			object = (DynamicExtensionBaseDomainObject) objectList.get(0);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		return object;
	}

	/**
	 * This method is called when there any exception occurs while generating the data table queries
	 * for the entity. Valid scenario is that if we need to fire Q1 Q2 and Q3 in order to create the
	 * data tables and Q1 Q2 get fired successfully and exception occurs while executing query Q3 then
	 * this method receives the query list which holds the set of queries which negate the effect of the
	 * queries which were generated successfully so that the metadata information and database are in
	 * synchronisation.
	 * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
	 * @param entity Entity
	 * @param e Exception encountered
	 * @param dao AbstractDAO
	 * @throws DynamicExtensionsSystemException
	 */
	protected void rollbackQueries(Stack reverseQueryStack, AbstractMetadataInterface abstractMetadata, Exception e, AbstractDAO dao)
			throws DynamicExtensionsSystemException
	{
		String message = "";
		/*try
		{*/
		dao.rollback();
		/*}
		catch (DAOException e2)
		{
			logDebug("rollbackQueries", DynamicExtensionsUtility.getStackTrace(e));
			DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(message, e);
			ex.setErrorCode(DYEXTN_S_000);
			throw ex;
		}*/

		if (reverseQueryStack != null && !reverseQueryStack.isEmpty())
		{
			Connection conn;
			try
			{
				conn = DBUtil.getConnection();
				while (!reverseQueryStack.empty())
				{
					String query = (String) reverseQueryStack.pop();
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
				LogFatalError(exc, abstractMetadata);
			}
			finally
			{
				logDebug("rollbackQueries", DynamicExtensionsUtility.getStackTrace(e));
				DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(message, e);
				ex.setErrorCode(DYEXTN_S_000);
				throw ex;
			}
		}
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
	protected Collection executeHQL(String queryName, Map<String, HQLPlaceHolderObject> substitutionParameterMap)
			throws DynamicExtensionsSystemException
	{
		Collection entityCollection = new HashSet();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);
			Session session = DBUtil.currentSession();
			Query query = substitutionParameterForQuery(session, queryName, substitutionParameterMap);
			entityCollection = query.list();
			//  hibernateDAO.commit();
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
				throw new DynamicExtensionsSystemException("Exception occured while closing the session", e, DYEXTN_S_001);
			}

		}
		return entityCollection;
	}

	/**
	 * This method substitues the parameters from substitutionParameterMap into the input query.
	 * @param substitutionParameterMap
	 * @throws HibernateException
	 */
	private Query substitutionParameterForQuery(Session session, String queryName, Map substitutionParameterMap) throws HibernateException
	{
		Query q = session.getNamedQuery(queryName);
		for (int counter = 0; counter < substitutionParameterMap.size(); counter++)
		{
			HQLPlaceHolderObject hPlaceHolderObject = (HQLPlaceHolderObject) substitutionParameterMap.get(counter + "");
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
			else if (objectType.equals("boolean"))
			{
				q.setBoolean(counter, Boolean.parseBoolean(hPlaceHolderObject.getValue() + ""));
			}
		}
		return q;

	}

	/**
	*
	* @param hibernateDAO
	* @param queryName
	* @param substitutionParameterMap
	* @return
	* @throws DynamicExtensionsSystemException
	*/
	protected Collection executeHQL(HibernateDAO hibernateDAO, String queryName, Map substitutionParameterMap)
			throws DynamicExtensionsSystemException
	{
		Collection entityCollection = new HashSet();

		try
		{
			Session session = DBUtil.currentSession();
			Query query = substitutionParameterForQuery(session, queryName, substitutionParameterMap);
			entityCollection = query.list();
		}
		catch (HibernateException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_001);
		}
		return entityCollection;

	}

	/**
	* This method persists an entity group and the associated entities and also creates the data table
	* for the entities.
	* @param entityGroupInterface entity group to be saved.
	* @return entityGroupInterface Saved  entity group.
	* @throws DynamicExtensionsSystemException
	* @throws DynamicExtensionsApplicationException
	*/
	protected AbstractMetadataInterface persistDynamicExtensionObject(AbstractMetadataInterface abstractMetdata)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List reverseQueryList = new LinkedList();
		List queryList = new ArrayList();
		Stack rollbackQueryStack = new Stack();

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			preProcess(abstractMetdata, reverseQueryList, hibernateDAO, queryList);

			saveDynamicExtensionObject(abstractMetdata, hibernateDAO, rollbackQueryStack);

			postProcess(queryList, reverseQueryList, rollbackQueryStack, hibernateDAO);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
		}
		catch (DynamicExtensionsSystemException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
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
				rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			}
		}
		return abstractMetdata;
	}

	/**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public AbstractMetadataInterface persistDynamicExtensionObjectMetdata(AbstractMetadataInterface abstractMetdata)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Stack rollbackQueryStack = new Stack();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			saveDynamicExtensionObject(abstractMetdata, hibernateDAO, rollbackQueryStack);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
			}
		}
		return abstractMetdata;
	}

	/**
	 * saveDynamicExtensionObject.
	 * @param abstractMetdata
	 * @param hibernateDAO
	 * @param rollbackQueryStack
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void saveDynamicExtensionObject(AbstractMetadataInterface abstractMetdata, HibernateDAO hibernateDAO, Stack rollbackQueryStack)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		try
		{
			if (abstractMetdata.getId() == null)
			{
				hibernateDAO.insert(abstractMetdata, null, false, false);
			}
			else
			{
				hibernateDAO.update(abstractMetdata, null, false, false, false);
			}
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
		}
		catch (UserNotAuthorizedException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
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
	 * @param e
	 * @param string
	 * @param hibernateDAO
	 * @throws DynamicExtensionsSystemException
	 */
	protected Exception handleRollback(Exception e, String exceptionMessage, AbstractDAO dao, boolean isExceptionToBeWrapped)
	{
		/*try
		{*/
		dao.rollback();
		/*}
		catch (DAOException e1)
		{
			return new DynamicExtensionsSystemException("error while rollback", e);
		}*/

		if (isExceptionToBeWrapped)
		{
			return new DynamicExtensionsSystemException(exceptionMessage, e);
		}
		else
		{
			return e;
		}
	}

	/**
	 * getDynamicQueryList.
	 */
	protected List getDynamicQueryList(EntityGroupInterface entityGroupInterface, List reverseQueryList, HibernateDAO hibernateDAO, List queryList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<EntityInterface> entityList = DynamicExtensionsUtility.getUnsavedEntities(entityGroupInterface);

		for (EntityInterface entityObject : entityList)
		{
			List createQueryList = getQueryBuilderInstance().getCreateEntityQueryList((Entity) entityObject, reverseQueryList, hibernateDAO);

			if (createQueryList != null && !createQueryList.isEmpty())
			{
				queryList.addAll(createQueryList);
			}
		}
		for (EntityInterface entityObject : entityList)
		{
			List createQueryList = getQueryBuilderInstance().getUpdateEntityQueryList((Entity) entityObject, reverseQueryList, hibernateDAO);

			if (createQueryList != null && !createQueryList.isEmpty())
			{
				queryList.addAll(createQueryList);
			}
		}
		List<EntityInterface> savedEntityList = DynamicExtensionsUtility.getSavedEntities(entityGroupInterface);

		for (EntityInterface savedEntity : savedEntityList)
		{
			Entity databaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, savedEntity.getId());

			List updateQueryList = getQueryBuilderInstance().getUpdateEntityQueryList((Entity) savedEntity, databaseCopy, reverseQueryList);

			if (updateQueryList != null && !updateQueryList.isEmpty())
			{
				queryList.addAll(updateQueryList);
			}
		}
		return queryList;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#getAllRecords(edu.common.dynamicextensions.domaininterface.EntityInterface)
	 */
	public List<EntityRecord> getAllRecords(AbstractEntityInterface entity) throws DynamicExtensionsSystemException
	{
		List<EntityRecord> recordList = new ArrayList<EntityRecord>();
		JDBCDAO jdbcDao = null;
		List<List> result;
		try
		{
			jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);
			TablePropertiesInterface tablePropertiesInterface = entity.getTableProperties();
			String tableName = tablePropertiesInterface.getName();
			String[] selectColumnName = {IDENTIFIER};
			String[] whereColumnName = {Constants.ACTIVITY_STATUS_COLUMN};
			String[] whereColumnCondition = {EQUAL};
			Object[] whereColumnValue = {"'" + Constants.ACTIVITY_STATUS_ACTIVE + "'"};
			result = jdbcDao.retrieve(tableName, selectColumnName, whereColumnName, whereColumnCondition, whereColumnValue, null);
			recordList = getRecordList(result);

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
		return recordList;
	}

	/**
	* @param result
	* @return
	*/
	protected List<EntityRecord> getRecordList(List<List> result)
	{
		List<EntityRecord> recordList = new ArrayList<EntityRecord>();
		EntityRecord entityRecord;
		String id;
		for (List innnerList : result)
		{
			if (innnerList != null && !innnerList.isEmpty())
			{
				id = (String) innnerList.get(0);
				if (id != null)
				{
					entityRecord = new EntityRecord(new Long(id));
					recordList.add(entityRecord);
				}
			}
		}
		return recordList;
	}
}