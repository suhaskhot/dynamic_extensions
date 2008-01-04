
package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 *
 * @author rajesh_patil
 * @author mandar_shidhore
 *
 */
public class CategoryManager extends AbstractMetadataManager implements CategoryManagerInterface
{
	/**
	 * Static instance of the CategoryManager.
	 */
	private static CategoryManagerInterface categoryManager = null;

	/**
	 * Static instance of the queryBuilder.
	 */
	private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

	/**
	 * Empty Constructor.
	 */
	protected CategoryManager()
	{

	}

	/**
	 * Returns the instance of the Entity Manager.
	 * @return entityManager singleton instance of the Entity Manager.
	 */
	public static synchronized CategoryManagerInterface getInstance()
	{
		if (categoryManager == null)
		{
			categoryManager = new CategoryManager();
			DynamicExtensionsUtility.initialiseApplicationVariables();
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
		}
		return categoryManager;
	}

	/**
	 * LogFatalError.
	 */
	protected void LogFatalError(Exception e, AbstractMetadataInterface abstractMetadata)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Method to persist categroy metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
        CategoryInterface category = (CategoryInterface) persistDynamicExtensionObject(categoryInterface);
        return category;
	}
	/**
	 * Method to persist categroy metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface persistCategoryMetadata(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
        CategoryInterface category = (CategoryInterface) persistDynamicExtensionObjectMetdata(categoryInterface);
        return category;
	}

	/**
	 * Method to delete category metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface deleteCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		logDebug("deleteCategory", "entering the method");
		Category category = (Category) categoryInterface;

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		Stack stack = new Stack();

		try
		{
			hibernateDAO.openSession(null);

			if (category.getId() != null)
				hibernateDAO.delete(category);

			hibernateDAO.commit();
		}
		catch (Exception e)
		{
			rollbackQueries(stack, category, e, hibernateDAO);

			if (e instanceof DynamicExtensionsApplicationException)
			{
				throw (DynamicExtensionsApplicationException) e;
			}
			else
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (Exception e)
			{
				rollbackQueries(stack, category, e, hibernateDAO);
			}
		}
		logDebug("persistCategory", "exiting the method");
		return category;
	}

	/**
	 *
	 */
	protected void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List reverseQueryList,
			HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) dynamicExtensionBaseDomainObject;

		queryList.addAll(getDynamicQueryList(category, reverseQueryList, hibernateDAO, queryList));
	}
	/**
	 * getDynamicQueryList.
	 */
	public List getDynamicQueryList(CategoryInterface category, List reverseQueryList,
			HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		return queryBuilder.getCreateCategoryQueryList((Category) category, reverseQueryList,
				hibernateDAO);
	}
	/**
	 * postProcess.
	 */
	protected void postProcess(List queryList, List reverseQueryList, Stack rollbackQueryStack) throws DynamicExtensionsSystemException
	{
		 queryBuilder.executeQueries(queryList, reverseQueryList, rollbackQueryStack);
	}
	/**
	 *
	 */
	public Long insertData(CategoryInterface category, Map<AbstractMetadataInterface, ?> dataValue) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Map<AbstractMetadataInterface, ?>> dataValueMapList = new ArrayList<Map<AbstractMetadataInterface, ?>>();
		dataValueMapList.add(dataValue);
		List<Long> recordIdList = insertData(category, dataValueMapList);
		return recordIdList.get(0);
	}
	/**
	 *
	 */
	public List<Long> insertData(CategoryInterface category, List<Map<AbstractMetadataInterface, ?>> dataValueMapList) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Long> recordIdList = new ArrayList<Long>();
		HibernateDAO hibernateDAO = null;
		try
		{
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);
			NewEntityManagerInterface newEntityManager = NewEntityManager.getInstance();
			for (Map<AbstractMetadataInterface, ?> dataValue : dataValueMapList)
			{
				EntityInterface entity = null;
				List<Map<AbstractAttributeInterface, ?>> dataValueMap = new ArrayList<Map<AbstractAttributeInterface, ?>>();
				recordIdList.addAll(newEntityManager.insertData(entity,dataValueMap));
			}

			hibernateDAO.commit();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw (DynamicExtensionsApplicationException) handleRollback(e,
					"Error while inserting data", hibernateDAO, false);
		}
		catch (Exception e)
		{
			throw (DynamicExtensionsSystemException) handleRollback(e,
					"Error while inserting data", hibernateDAO, true);
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw (DynamicExtensionsSystemException) handleRollback(e, "Error while closing",
						hibernateDAO, true);
			}
		}

		return recordIdList;
	}
	/**
	 *
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
	}

}