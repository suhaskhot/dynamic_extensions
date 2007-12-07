package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.EntityGroup;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author mandar_shidhore
 *
 */
public class NewEntityManager extends AbstractManager implements NewEntityManagerInterface, EntityManagerExceptionConstantsInterface {

    /**
     * Static instance of the entity manager.
     */
    private static NewEntityManagerInterface entityManager = null;


    /**
     * Static instance of the queryBuilder.
     */
    private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

    /**
     * Empty Constructor.
     */
    protected NewEntityManager()
    {
    }

    /**
     * Returns the instance of the Entity Manager.
     * @return entityManager singleton instance of the Entity Manager.
     */
    public static synchronized NewEntityManagerInterface getInstance()
    {
        if (entityManager == null)
        {
            entityManager = new NewEntityManager();
            DynamicExtensionsUtility.initialiseApplicationVariables();
            queryBuilder = QueryBuilderFactory.getQueryBuilder();
        }

        return entityManager;
    }

    /**
     * Mock entity manager can be placed in the entity manager using this method.
     * @param entityManager
     */
    public static void setInstance(NewEntityManagerInterface entityManagerInterface)
    {
        NewEntityManager.entityManager = entityManagerInterface;

    }
    /**
     * Saves the entity into the database.Also prepares the dynamic tables and associations
     * between those tables using the metadata information in the entity object.
     * EntityInterface can be obtained from DomainObjectFactory.
     * @param entityInterface
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    public EntityInterface persistEntity(EntityInterface entity)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
        List reverseQueryList = new LinkedList();
		List queryList = new ArrayList();
		Stack rollbackQueryStack = new Stack();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			preProcess(entity, queryList, hibernateDAO, reverseQueryList);

			if (entity.getId() == null)
			{
				hibernateDAO.insert(entity, null, false, false);
			}
			else
			{
				hibernateDAO.update(entity, null, false, false, false);
			}

			postProcess(queryList, reverseQueryList, rollbackQueryStack);

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, entity, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
		}
		catch (UserNotAuthorizedException e)
		{
			rollbackQueries(rollbackQueryStack, entity, e, hibernateDAO);
			e.printStackTrace();
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
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
		return entity;
	}
    /**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManagerInterface#persistEntityMetadata(edu.common.dynamicextensions.domaininterface.EntityInterface)
	 */
	public EntityInterface persistEntityMetadata(EntityInterface entity)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Stack rollbackQueryStack = new Stack();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			if (entity.getId() == null)
			{
				hibernateDAO.insert(entity, null, false, false);
			}
			else
			{
				hibernateDAO.update(entity, null, false, false, false);
			}

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, entity, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
		}
		catch (UserNotAuthorizedException e)
		{
			rollbackQueries(rollbackQueryStack, entity, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
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
		return entity;
	}
    /**
     * Save entity group which in turn saves the whole hierarchy.
     * @throws DynamicExtensionsSystemException
     * @throws DAOException
     * @throws DynamicExtensionsSystemException
     * @throws DAOException
     * @throws DynamicExtensionsApplicationException
     */
    public void saveEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
    {
        List reverseQueryList = new LinkedList();
        List queryList = new ArrayList();
        Stack rollbackQueryStack = new Stack();
        HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

        try
        {
            hibernateDAO.openSession(null);

            preProcess(group, queryList, hibernateDAO, reverseQueryList);


            preProcess(group, reverseQueryList, hibernateDAO, queryList);

            if (group.getId() == null)
            {
                hibernateDAO.insert(group, null, false, false);
            }
            else
            {
                hibernateDAO.update(group, null, false, false, false);
            }


            postProcess(queryList, reverseQueryList, rollbackQueryStack);

            hibernateDAO.commit();

        }
        catch (DAOException e)
        {
            rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
            throw new DynamicExtensionsSystemException(
                    "DAOException occured while opening a session to save the container.", e);
        }
        catch (DynamicExtensionsApplicationException e)
        {
            rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
            throw new DynamicExtensionsApplicationException(
                    "DAOException occured while opening a session to save the container.", e);
        }
        catch (DynamicExtensionsSystemException e)
        {
            rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
            e.printStackTrace();
            throw e;
        }
        catch (UserNotAuthorizedException e)
        {
            rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
            e.printStackTrace();
            throw new DynamicExtensionsSystemException(
                    "DAOException occured while opening a session to save the container.", e);
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
    }
    /**
	 * This method persists an entity group and the associated entities without creating the data table
	 * for the entities.
	 * @param entityGroupInterface entity group to be saved.
	 * @return entityGroupInterface Saved  entity group.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public EntityGroupInterface persistEntityGroupMetadata(EntityGroupInterface entityGroup)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Stack rollbackQueryStack = new Stack();
		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
				Constants.HIBERNATE_DAO);
		try
		{
			hibernateDAO.openSession(null);

			if (entityGroup.getId() == null)
			{
				hibernateDAO.insert(entityGroup, null, false, false);
			}
			else
			{
				hibernateDAO.update(entityGroup, null, false, false, false);
			}

			hibernateDAO.commit();
		}
		catch (DAOException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
		}
		catch (UserNotAuthorizedException e)
		{
			rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
			throw new DynamicExtensionsSystemException(
					"DAOException occured while opening a session to save the container.", e);
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
		return entityGroup;
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
    private void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List reverseQueryList, HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
    {
    	if (dynamicExtensionBaseDomainObject instanceof EntityGroupInterface)
		{
    		EntityGroupInterface entityGroup = (EntityGroupInterface) dynamicExtensionBaseDomainObject;

			createDynamicQueries(entityGroup, reverseQueryList, hibernateDAO, queryList);
		}
    	else if (dynamicExtensionBaseDomainObject instanceof EntityInterface)
    	{
    		EntityInterface entity = (EntityInterface) dynamicExtensionBaseDomainObject;

			createDynamicQueries(entity, reverseQueryList, hibernateDAO, queryList);
    	}
    }

    /**
     * This method executes dynamic table queries created for all the entities within a group.
     * @param queryList List of queries to be executed to created dynamicn tables.
     * @param reverseQueryList List of queries to be executed in case any problem occurs at DB level.
     * @param rollbackQueryStack Stack to undo any changes done beforehand at DB level.
     * @throws DynamicExtensionsSystemException
     */
    private void postProcess(List queryList, List reverseQueryList, Stack rollbackQueryStack) throws DynamicExtensionsSystemException
    {
        queryBuilder.executeQueries(queryList, reverseQueryList, rollbackQueryStack);
    }

    private List createDynamicQueries(EntityGroupInterface group, List reverseQueryList, HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
    {
        for (EntityInterface entityInterface: group.getEntityCollection())
        {
        	getDynamicQueryList(entityInterface,reverseQueryList,hibernateDAO,queryList);
        }
        return queryList;
    }

	private List createDynamicQueries(EntityInterface entityInterface, List reverseQueryList, HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
        return getDynamicQueryList(entityInterface,reverseQueryList,hibernateDAO,queryList);
	}

	private List getDynamicQueryList(EntityInterface entityInterface, List reverseQueryList, HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
        if (entityInterface.getId() == null)
        {
        	List createQueryList = queryBuilder.getCreateEntityQueryList((Entity)entityInterface, reverseQueryList, hibernateDAO, false);

        	if (createQueryList != null && !createQueryList.isEmpty())
        	{
        		queryList.add(createQueryList.get(0));
        	}
        }
        else
        {
            Entity databaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, entityInterface.getId());

            List updateQueryList = queryBuilder.getUpdateEntityQueryList((Entity)entityInterface, (Entity) databaseCopy, reverseQueryList);

            if (updateQueryList != null && !updateQueryList.isEmpty())
            {
                queryList.add(queryBuilder.getUpdateEntityQueryList((Entity)entityInterface, (Entity) databaseCopy, reverseQueryList).get(0));
            }
        }
        return queryList;
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
    private void rollbackQueries(Stack reverseQueryStack, EntityInterface entity, Exception e, AbstractDAO dao)
            throws DynamicExtensionsSystemException
    {
        String message = "";
        try
        {
            dao.rollback();
        }
        catch (DAOException e2)
        {
            logDebug("rollbackQueries", DynamicExtensionsUtility.getStackTrace(e));
            DynamicExtensionsSystemException ex = new DynamicExtensionsSystemException(message, e);
            ex.setErrorCode(DYEXTN_S_000);
            throw ex;
        }

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
                LogFatalError(exc, entity);
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
     * This method is used to log the messages in a uniform manner. The method takes the string method name and
     * string message. Using these parameters the method formats the message and logs it.
     * @param methodName Name of the method for which the message needs to be logged.
     * @param message The message that needs to be logged.
     */
    private void logDebug(String methodName, String message)
    {
        Logger.out.debug("[NewEntityManager.]" + methodName + "() -- " + message);
    }

    /**
     * This method is called when exception occurs while executing the rollback queries
     * or reverse queries. When this method is called , it signifies that the database state
     * and the metadata state for the entity are not in synchronisation and administrator
     * needs some database correction.
     * @param e The exception that took place.
     * @param entity Entity for which data tables are out of sync.
     */
    private void LogFatalError(Exception e, EntityInterface entity)
    {
        String table = "";
        String name = "";
        if (entity != null)
        {
            entity.getTableProperties().getName();
            name = entity.getName();
        }
        Logger.out.error("***Fatal Error.. Incosistent data table and metadata information for the entity -" + name + "***");
        Logger.out.error("Please check the table -" + table);
        Logger.out.error("The cause of the exception is - " + e.getMessage());
        Logger.out.error("The detailed log is : ");
        e.printStackTrace();
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
        //Getting the instance of the default biz logic class which has the method that returns the particular object
        //depending on the value of a particular column of the associated table.
        DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();

        try
        {
            //Calling retrieve method to  get the entity group object based on the given value of short name.
            //Passed parameters are the class name of the entity group class, the name of the hibernate object member variable
            // and the value of that member variable.
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
    public Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
    {
        Map substitutionParameterMap = new HashMap();
        substitutionParameterMap.put("0", new HQLPlaceHolderObject("long", sourceEntityId));
        substitutionParameterMap.put("1", new HQLPlaceHolderObject("long", targetEntityId));
        //Following method is called to execute the stored HQL , the name of which is given as the first parameter.
        //The second parameter is the map which contains the actual values that are replaced for the placeholders.
        Collection associationCollection = executeHQL("getAssociations", substitutionParameterMap);
        return associationCollection;
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
    private Collection executeHQL(String queryName,
            Map<String, HQLPlaceHolderObject> substitutionParameterMap)
            throws DynamicExtensionsSystemException
    {
        Collection entityCollection = new HashSet();
        HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(
                Constants.HIBERNATE_DAO);
        try
        {
            hibernateDAO.openSession(null);
            Session session = DBUtil.currentSession();
            Query query = substitutionParameterForQuery(session,queryName, substitutionParameterMap);
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
                throw new DynamicExtensionsSystemException(
                        "Exception occured while closing the session", e, DYEXTN_S_001);
            }

        }
        return entityCollection;
    }
    /**
     * This method substitues the parameters from substitutionParameterMap into the input query.
     * @param substitutionParameterMap
     * @throws HibernateException
     */
    private Query substitutionParameterForQuery(Session session,String queryName, Map substitutionParameterMap)
            throws HibernateException
    {
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
            else if (objectType.equals("boolean"))
            {
                q.setBoolean(counter, Boolean.parseBoolean(hPlaceHolderObject.getValue() + ""));
            }
        }
        return q;

    }
    /**
     * This method returns the EntityInterface given the entity name.
     * @param entityGroupShortName
     * @return
     */
    public EntityInterface getEntityByName(String entityName)
            throws DynamicExtensionsSystemException
    {
        EntityInterface entityInterface = (EntityInterface) getObjectByName(Entity.class.getName(),
                entityName);
        return entityInterface;
    }
}