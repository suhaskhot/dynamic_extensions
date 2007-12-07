package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
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
     * Save entity group which in turn saves the whole hierarchy.
     * @throws DynamicExtensionsSystemException 
     * @throws DAOException 
     */
    public void saveEntityGroup(EntityGroupInterface group) throws DynamicExtensionsSystemException, DAOException
    {
        List reverseQueryList = new LinkedList();
        List queryList = new ArrayList();
        Stack rollbackQueryStack = new Stack();
        HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
        
        try
        {
            hibernateDAO.openSession(null);
            
            preProcess(group, reverseQueryList, hibernateDAO, queryList);
            
            if (group.getId() == null)
            {
                hibernateDAO.insert(group, null, false, false);
            }
            else
            {
                hibernateDAO.update(group, null, false, false, false);
            }
            
            hibernateDAO.commit();
            
            postProcess(queryList, reverseQueryList, rollbackQueryStack);    
        }
        catch(Exception e)
        {
            e.printStackTrace();
            try 
            {
                hibernateDAO.rollback();
                rollbackQueries(rollbackQueryStack, null, e, hibernateDAO);
            } 
            catch (DynamicExtensionsSystemException e1) 
            {
                throw new DynamicExtensionsSystemException("Exception while saving metadata");
            } 
            catch (DAOException daoException) 
            {
                throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
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
                throw new DynamicExtensionsSystemException("DAOException occured while opening a session to save the container.", e);
            }
        }
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
    private void preProcess(EntityGroupInterface group, List reverseQueryList, HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
    {
        createDynamicQueries(group, reverseQueryList, hibernateDAO, queryList);
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
            if (entityInterface.getId() == null)
            {
                queryList.add(queryBuilder.getCreateEntityQueryList((Entity)entityInterface, reverseQueryList, hibernateDAO, false).get(0));
            }
            else
            {
                Entity databaseCopy = (Entity) DBUtil.loadCleanObj(Entity.class, entityInterface.getId());
                if (queryBuilder.getUpdateEntityQueryList((Entity)entityInterface, (Entity) databaseCopy, reverseQueryList).size() != 0)
                {
                    queryList.add(queryBuilder.getUpdateEntityQueryList((Entity)entityInterface, (Entity) databaseCopy, reverseQueryList).get(0));
                }
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
    private void rollbackQueries(Stack reverseQueryList, Entity entity, Exception e, AbstractDAO dao)
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
        Logger.out.debug("[EntityManager.]" + methodName + "()--" + message);
    }
    
    /**
     * This method is called when exception occurs while executing the rollback queries 
     * or reverse queries. When this method is called , it signifies that the database state 
     * and the metadata state for the entity are not in synchronisation and administrator 
     * needs some database correction.
     * @param e The exception that took place.
     * @param entity Entity for which data tables are out of sync.
     */
    private void LogFatalError(Exception e, Entity entity)
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

}